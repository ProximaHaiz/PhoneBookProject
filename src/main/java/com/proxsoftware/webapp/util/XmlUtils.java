package com.proxsoftware.webapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Proxima on 22.04.2016.
 */
//@Component
//@Configuration
@PropertySource("${classpath:app.properties}")
@Component
public class XmlUtils implements InitializingBean {

    @Value(value = "${file_uri}")
    private String FILE_URI;
    private XStream xStream;
    private ObjectMapper mapper;
    private File file;


    private HierarchicalStreamDriver driver;
    @Value(value = "${file_store_name}")
    String storee;

    @Resource
    public Environment env;


    FileStoreEnum store;

    public XmlUtils() {
        FILE_URI = "test1.json";
        store = FileStoreEnum.valueOf(getFileExtension(new File(FILE_URI)).toUpperCase());
        switch (store) {
            case TXT:
                driver = new StaxDriver();
                System.out.println(driver.getClass());
                break;
            case XML:
                driver = new StaxDriver();
                System.out.println(driver.getClass());

                break;
            case JSON:
                mapper = new ObjectMapper();
                System.out.println(mapper.getClass());
                break;
            default:
                FILE_URI = "data.json";
                mapper = new ObjectMapper();
        }
        xStream = new XStream(driver);
        xStream.processAnnotations(new Class[]{AccountEntity.class, ContactEntity.class});
//        xStream.registerConverter(new MapEntryConverter());
        file = new File(FILE_URI);
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    //<editor-fold desc="getter\setter">
    public FileStoreEnum getStore() {
        return store;
    }

    public void setStore(FileStoreEnum store) {
        this.store = store;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    public String getFILE_URI() {
        return FILE_URI;
    }

    public void setFILE_URI(String FILE_URI) {
        this.FILE_URI = FILE_URI;
    }

    public XStream getxStream() {
        return xStream;
    }

    public void setxStream(XStream xStream) {
        this.xStream = xStream;
    }

    public HierarchicalStreamDriver getDriver() {
        return driver;
    }

    public void setDriver(HierarchicalStreamDriver driver) {
        this.driver = driver;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        store = FileStoreEnum.valueOf(getFileExtension(new File(FILE_URI)).toUpperCase());
        switch (store) {
            case TXT:
                driver = new StaxDriver();
                System.out.println(driver.getClass());
                break;
            case XML:
                driver = new StaxDriver();
                System.out.println(driver.getClass());

                break;
            case JSON:
                mapper = new ObjectMapper();
                System.out.println(mapper.getClass());
                break;
            default:
                FILE_URI = "data.json";
                mapper = new ObjectMapper();
        }
        xStream = new XStream(driver);
        xStream.processAnnotations(new Class[]{AccountEntity.class, ContactEntity.class});
        xStream.registerConverter(new MapEntryConverter());
        file = new File(FILE_URI);
    }

    class MapEntryConverter implements Converter {
        @Override
        public boolean canConvert(Class clazz) {
            return AbstractMap.class.isAssignableFrom(clazz);
        }

        @Override
        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
            AbstractMap<String, String> map = (AbstractMap<String, String>) value;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writer.startNode(entry.getKey());
                writer.setValue(entry.getValue());
                writer.endNode();
            }
        }
        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            Map<String, String> map = new HashMap<String, String>();

            while (reader.hasMoreChildren()) {
                reader.moveDown();
                map.put(reader.getNodeName(), reader.getValue());
                reader.moveUp();
            }
            return map;
        }
        //<editor-fold desc="MapEntry">
   /* public static class MapEntryConverter implements Converter {

        @Override
        public boolean canConvert(Class clazz) {
            return AbstractMap.class.isAssignableFrom(clazz);
        }

        @Override
        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

            AbstractMap map = (AbstractMap) value;
            for (Object obj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                writer.startNode(entry.getKey().toString());
                Object val = entry.getValue();
                if (null != val) {
                    writer.setValue(val.toString());
                }
                writer.endNode();
            }

        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

            Map<String, String> map = new HashMap<String, String>();

            while (reader.hasMoreChildren()) {
                reader.moveDown();

                String key = reader.getNodeName(); // nodeName aka element's name
                String value = reader.getValue();
                map.put(key, value);

                reader.moveUp();
            }

            return map;
        }*/
        //</editor-fold>
    }
}

//</editor-fold>



