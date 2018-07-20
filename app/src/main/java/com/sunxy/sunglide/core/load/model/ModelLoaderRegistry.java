package com.sunxy.sunglide.core.load.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class ModelLoaderRegistry {

    private List<Entry<?,?>> entries = new ArrayList<>();

    /**
     * 注册    String/File->InputStream
     *
     * @param modelClass  数据来源类型
     * @param dataClass   数据转换后的类型
     * @param factory     创建modelLoader的工厂
     */
    public synchronized <Model, Data> void add(Class<Model> modelClass, Class<Data> dataClass,
                                               ModelLoader.ModelLoaderFactory<Model, Data> factory){
        entries.add(new Entry<>(modelClass, dataClass, factory));
    }

    /**
     * 获得 对应model与data类型的modelLoader
     * @param modelClass
     * @param dataClass
     * @param <Model>
     * @param <Data>
     * @return
     */
    public <Model, Data> ModelLoader<Model, Data> build(Class<Model> modelClass, Class<Data> dataClass){
        List<ModelLoader<Model, Data>> loaders = new ArrayList<>();
        for (Entry<?, ?> entry : entries) {
            //是我们需要的model与data类型的loader
            if (entry.handles(modelClass, dataClass)){
                loaders.add((ModelLoader<Model, Data>) entry.factory.build(this));
            }
        }
        if (loaders.size() > 1){
            return new MultiModelLoader(loaders);
        }else if(loaders.size() == 1){
            return loaders.get(0);
        }
        throw new RuntimeException("No Match:" + modelClass.getName() + " Data:" + dataClass.getName());
    }

    /**
     * 查找对应model类型的modelLoader
     * @param modelClass
     * @param <Model>
     * @return
     */
    public <Model> List<ModelLoader<Model, ?>> getModelLoaders(Class<Model> modelClass){
        List<ModelLoader<Model, ?>> loaders = new ArrayList<>();
        for (Entry<?, ?> entry : entries) {
            if (entry.handles(modelClass)) {
                loaders.add((ModelLoader<Model, ?>) entry.factory.build(this));
            }
        }
        return loaders;
    }

    private static class Entry<Model, Data>{
        Class<Model> modelClass;
        Class<Data> dataClass;
        ModelLoader.ModelLoaderFactory<Model, Data> factory;

        public Entry(Class<Model> modelClass, Class<Data> dataClass, ModelLoader.ModelLoaderFactory<Model, Data> factory){
            this.modelClass = modelClass;
            this.dataClass = dataClass;
            this.factory = factory;
        }

        boolean handles(Class<?> modelClass, Class<?> dataClass){
            //A.isAssignableFrom(B) B和A是同一个类型 或者 B是A的子类
            return this.modelClass.isAssignableFrom(modelClass)
                    && this.dataClass.isAssignableFrom(dataClass);
        }

        boolean handles(Class<?> modelClass){
            return this.modelClass.isAssignableFrom(modelClass);
        }
    }


}
