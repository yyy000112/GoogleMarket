package android.ye.googlemarket.Http.protocol;

import android.ye.googlemarket.Http.HttpHelper;
import android.ye.googlemarket.Utils.IOUtils;
import android.ye.googlemarket.Utils.StringUtils;
import android.ye.googlemarket.Utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 网络数据请求的封装
 * Created by ye on 2016/11/10.
 */
public abstract class BaseProtocol<T> {

    public T getData(int index){
        //先判断缓存
        String result = getCache(index);
        //如果没有缓存，请求服务器
        if (StringUtils.isEmpty(result)){
           result =  getDataFromServer(index);
        }
        //result 不为空，开始解析数据
        if (result!=null){
          T data = parseData(result);
            return data;
        }
        return null;
    }




    /**
     * 从网络获取数据
     * @param index 表示的是从哪个位置开始返回20条数据, 用于分页
     * @return
     */
    private String getDataFromServer(int index) {

        HttpHelper.HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey() + "?index=" + index + getParams());
        if (httpResult != null){
            String result = httpResult.getString();

            //判断result不为空时写入缓存
            if (!StringUtils.isEmpty(result)){
                setCache(index,result);
            }
            return result;
        }
        return null;
    }

    /**
     * 写缓存
     * @param index
     * @param json
     */
    public void setCache(int index, String json) {
        //获取手机文件夹
        File cacheDir = UIUtils.getContext().getCacheDir();
        //建立缓存文件
        File cacheFile = new File(cacheDir,getKey()+"?index="+ index+getParams());
        //写入缓存
        FileWriter writer = null;
        try {
            writer = new FileWriter(cacheFile);
            //缓存失效的截止时间,一小时有效时间
            long deadLine = System.currentTimeMillis()+60*60*1000;
            //在第一行写入缓存时间并换行
            writer.write(deadLine + "\n");
            //写入json
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(writer);
        }

    }

    /**
     * 读缓存
     * @param index
     * @return
     */
    public String getCache(int index) {
        //获取手机文件夹
        File cacheDir = UIUtils.getContext().getCacheDir();
        //建立缓存文件
        File cacheFile = new File(cacheDir,getKey()+"?index="+ index+getParams());
        //判断缓存是否存在
        if (cacheFile.exists()){
            BufferedReader reader = null;
            //判断缓存是否有效
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                //读取第一行的缓存时间
                String deadLine = reader.readLine();
                //将字符串转换成long类型
                long deadTime = Long.parseLong(deadLine);
                if (System.currentTimeMillis()< deadTime){
                    //时间小于缓存的截止时间说明缓存有效
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = reader.readLine())!=null){
                        sb.append(line);
                    }
                    return sb.toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }

    //获取网络参数，由子类实现
    public abstract String getParams();

    //获取网络关键词，由子类实现
    public abstract String getKey();

    //数据解析由子类实现
    public abstract T parseData(String result);

}
