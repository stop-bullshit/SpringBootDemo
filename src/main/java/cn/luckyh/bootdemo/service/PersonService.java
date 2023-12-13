package cn.luckyh.bootdemo.service;

import cn.luckyh.bootdemo.mapper.PersonMapper;
import cn.luckyh.bootdemo.model.dto.PersonDTO;
import cn.luckyh.bootdemo.model.entity.Person;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonService {

    @Resource
    private FileService fileService;

    @Resource
    private PersonMapper personMapper;


    @Resource
    private ThreadPoolTaskExecutor asyncExecutorBase;

    @Async
//    @Transactional
    public CompletableFuture<Void> upload() {
        // 创建一个 StopWatch 实例来测量执行时间
        StopWatch mainStopWatch = new StopWatch();
        mainStopWatch.start();
        try {
            // 创建一个 StopWatch 实例来测量文件上传任务的执行时间
            StopWatch fileUploadStopWatch = new StopWatch();
            fileUploadStopWatch.start();
            // 异步上传文件到文件服务，获取文件ID
            CompletableFuture<String> fileIdFuture = CompletableFuture.supplyAsync(() -> {
                try {

                    String fileId = fileService.uploadFile();
                    fileUploadStopWatch.stop();
                    System.out.println("文件上传任务执行时间：" + fileUploadStopWatch.getTotalTimeMillis() + " 毫秒");
                    return fileId;
                } catch (Exception e) {
                    // 处理文件上传失败的情况
                    // 可以记录日志或抛出自定义异常
                    fileUploadStopWatch.stop();
                    System.out.println("文件上传任务执行时间：" + fileUploadStopWatch.getTotalTimeMillis() + " 毫秒");
                    return null;
                }
            });

            // 创建一个 StopWatch 实例来测量数据库查询任务的执行时间
            StopWatch dbQueryStopWatch = new StopWatch();
            dbQueryStopWatch.start();
            // 异步查询数据库并获取对象数据
            CompletableFuture<Person> databaseQueryFuture = CompletableFuture.supplyAsync(() -> {
                try {

                    Person byId = personMapper.findById();
                    dbQueryStopWatch.stop();
                    System.out.println("数据库查询任务执行时间：" + dbQueryStopWatch.getTotalTimeMillis() + " 毫秒");
                    return byId;
                } catch (Exception e) {
                    // 处理数据库查询失败的情况
                    // 可以记录日志或抛出自定义异常
                    dbQueryStopWatch.stop();
                    System.out.println("数据库查询任务执行时间：" + dbQueryStopWatch.getTotalTimeMillis() + " 毫秒");
                    return null;
                }
            });

            // 等待文件上传和数据库查询的结果
            String fileId = fileIdFuture.join();
            Person existingObject = databaseQueryFuture.join();

            // 检查文件上传和数据库查询是否成功
            if (fileId != null && existingObject != null) {
                // 修改对象的状态属性
                existingObject.setStatus(1);

                // 保存文件ID到对象中
                existingObject.setFileId(fileId);

                // 执行对象的更新操作
                personMapper.save(existingObject);
            } else {
                // 处理文件上传失败、数据库查询失败或对象不存在的情况
                log.error("文件上传失败");
                // 可以记录日志或抛出自定义异常
            }
        } catch (Exception e) {
            // 处理其他异常情况
            // 可以记录日志或抛出自定义异常
        } finally {
            // 停止计时并记录执行时间
            mainStopWatch.stop();
            System.out.println("方法执行时间：" + mainStopWatch.getTotalTimeMillis() + " 毫秒");
        }


        return CompletableFuture.completedFuture(null);
    }

    public void threadTst(Integer num) {
        log.info("入参:{}", num);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 模拟需要批量处理的更新操作
        for (int i = 0; i < num; i++) {
            final int taskId = i;
            asyncExecutorBase.submit(() -> {
                // 执行更新操作，这里是示例代码
                System.out.println("Updating task: " + taskId + " in thread: " + Thread.currentThread().getName());
            });
        }
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        System.out.println("执行时间：" + totalTimeMillis + " 毫秒");
        // 关闭线程池
        asyncExecutorBase.shutdown();
    }


    public void threadTst2(Integer num) {
        log.info("入参:{}", num);
        List<PersonDTO> personList = generateRandomPersonList(num, 10, 80);
        CopyOnWriteArrayList<PersonDTO> saveList = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<PersonDTO> updateList = new CopyOnWriteArrayList<>();

        asyncProcessList(personList, saveList, updateList);

//        System.out.println(saveList.size());
    }

    public void asyncProcessList(List<PersonDTO> voList, CopyOnWriteArrayList<PersonDTO> saveList, CopyOnWriteArrayList<PersonDTO> updateList) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("async-partitionList-for");
        List<List<PersonDTO>> partitionList = Lists.partition(voList, 1000);
        CountDownLatch countDownLatch = new CountDownLatch(voList.size());
        AtomicInteger integer = new AtomicInteger(0);
        partitionList.forEach(personDTOS -> {
            personDTOS.forEach(dto -> {
                asyncExecutorBase.submit(() -> {
                    processTaskDetail(dto, saveList, updateList, countDownLatch, integer);
                });
//                processTaskDetail(dto, saveList, updateList, countDownLatch);
            });
//            System.out.println("【数据核销】任务{}执行结束,剩余" + countDownLatch.getCount());
        });

        try {
            countDownLatch.await();

            stopWatch.stop();

            log.info("【数据核销】总数: {} ", integer.get());
        } catch (InterruptedException e) {
            log.error("【数据核销】CountDownLatch等待任务异常，ExceptionName: {} Error: {}");
        }
//        throw new RuntimeException("测试异常");
        // StopWatch stopWatch = new StopWatch();
        // stopWatch.start("asyncProcessList");
//        List<CompletableFuture<Void>> futures = new ArrayList<>();
//
//        for (List<PersonDTO> simpleWaybillQueryVos : Lists.partition(voList, 2000)) {
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                simpleWaybillQueryVos.forEach(waybillQueryVo -> {
//
//                    processTaskDetail(waybillQueryVo, saveList, updateList, countDownLatch);
//                });
//            }, asyncExecutorBase);
//
//            futures.add(future);
//        }
//
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//        allOf.join(); // 等待所有任务完成
//        stopWatch.stop();
//        log.info("【数据核销】异步执行结束耗时: {} 毫秒", stopWatch.getTotalTimeMillis());
        CountDownLatch latch = new CountDownLatch(voList.size());
        AtomicInteger asyncForInteger = new AtomicInteger(0);
        stopWatch.start("async-for");
        voList.forEach(
                dto -> {
                    asyncExecutorBase.submit(() -> {
                        processTaskDetail(dto, saveList, updateList, latch, asyncForInteger);
//                processTaskDetail(dto, saveList, updateList, countDownLatch);
                    });
//            System.out.println("【数据核销】任务{}执行结束,剩余" + countDownLatch.getCount());
                });

        try {
            latch.await();
            log.info("【数据核销】总数: {} ", asyncForInteger.get());
            stopWatch.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopWatch.start("for执行");
        AtomicInteger forInteger = new AtomicInteger(0);
        voList.forEach(dto -> {
            processTaskDetail(dto, saveList, updateList, latch, forInteger);
        });
        log.info("【数据核销】总数: {} ", forInteger.get());
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }


    public List<PersonDTO> generateRandomPersonList(int count, int minAge, int maxAge) {
        return ThreadLocalRandom.current()
                .ints(count, minAge, maxAge + 1)
                .mapToObj(age -> new PersonDTO("name" + age, age))
                .collect(Collectors.toList());
    }

    public void processTaskDetail(PersonDTO waybillQueryVo, CopyOnWriteArrayList<PersonDTO> saveList, CopyOnWriteArrayList<PersonDTO> updateList, CountDownLatch countDownLatch, AtomicInteger integer) {
        // StopWatch tStopWatch = new StopWatch();
        // tStopWatch.start(waybillQueryVo.getDeclarationWaybillId());
        try {
            // 判断需要保存还是更新
            checkUpdateOrSave(waybillQueryVo, saveList, updateList);
        } catch (Exception e) {
            // 异常处理逻辑
//            log.error("【数据核销】异步处理清关状态数据时异常 消息数据{}", waybillQueryVo);
            integer.incrementAndGet();
        } finally {
            integer.incrementAndGet();
            countDownLatch.countDown();
//            System.out.println("【数据核销】任务{}执行结束,剩余" + countDownLatch.getCount());
//         tStopWatch.stop();
//         log.info("【数据核销】任务{}耗时: {} 毫秒", tStopWatch.getLastTaskName(), tStopWatch.getTotalTimeMillis());
        }
    }

    private void checkUpdateOrSave(PersonDTO waybillQueryVo, CopyOnWriteArrayList<PersonDTO> saveList, CopyOnWriteArrayList<PersonDTO> updateList) {
        // 保存到新增数据List
        waybillQueryVo.setName(waybillQueryVo.getName() + "new");
//        log.info("【数据核销】当前数据需要保存:{}", waybillQueryVo);
        saveList.add(waybillQueryVo);
        updateList.add(waybillQueryVo);
//        if (waybillQueryVo.getAge() > 30) {
//         保存到更新数据List
//            throw new RuntimeException("测试异常");
//        }

    }

    public void threadTst3(Integer num) {
        log.info("入参:{}", num);
        List<PersonDTO> personList = generateRandomPersonList(num, 10, 80);
        personList.forEach(personDTO -> {
            CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    log.info("future: {}", personDTO);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, asyncExecutorBase);
        });
        personList.forEach(personDTO -> System.out.println("打印信息:{}" + personDTO));
    }
}