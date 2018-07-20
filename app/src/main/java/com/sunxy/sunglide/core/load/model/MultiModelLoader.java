package com.sunxy.sunglide.core.load.model;

import java.util.List;

/**
 * --
 * <p>
 * Created by sunxy on 2018/7/20 0020.
 */
public class MultiModelLoader<Model, Data> implements ModelLoader<Model, Data> {


    private List<ModelLoader<Model, Data>> modelLoaders;

    public MultiModelLoader(List<ModelLoader<Model, Data>> modelLoaders) {
        this.modelLoaders = modelLoaders;
    }

    @Override
    public boolean handles(Model model) {
        for (ModelLoader<Model, Data> modelLoader : modelLoaders) {
            if (modelLoader.handles(model)){
                return true;
            }
        }
        return false;
    }

    @Override
    public LoadData<Data> buildData(Model model) {
        for (int i = 0; i < modelLoaders.size(); i++) {
            ModelLoader<Model, Data> modelLoader = modelLoaders.get(i);
            if (modelLoader.handles(model)){
                return modelLoader.buildData(model);
            }
        }
        return null;
    }
}
