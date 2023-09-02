package cn.luckyh.bootdemo.service;

import cn.luckyh.bootdemo.mapper.PersonMapper;
import cn.luckyh.bootdemo.model.entity.Person;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PersonService {

    @Resource
    private FileService fileService;

    @Resource
    private PersonMapper personMapper;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

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
            threadPoolTaskExecutor.submit(() -> {
                // 执行更新操作，这里是示例代码
                System.out.println("Updating task: " + taskId + " in thread: " + Thread.currentThread().getName());
            });
        }
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        System.out.println("执行时间：" + totalTimeMillis + " 毫秒");
        // 关闭线程池
        threadPoolTaskExecutor.shutdown();
    }
}
