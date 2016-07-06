/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.library.mvp.demo;

import com.example.library.mvp.BasePresenter;
import com.example.library.mvp.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 * 官方的实现中加入了契约类来统一管理view与presenter的所有的接口，
 * 这种方式使得view与presenter中有哪些功能，
 * 一目了然，维护起来也方便
 */
public interface FDemoMVPContract {

    interface View extends BaseView<Presenter> {


        void hideTitle();

        void showTitle(String title);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void editTask();

        void deleteTask();

    }
}
