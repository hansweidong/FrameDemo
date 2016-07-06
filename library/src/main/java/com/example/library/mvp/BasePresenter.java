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

package com.example.library.mvp;

/**
 * BasePresenter中含有方法start(),
 * 该方法的作用是presenter开始获取数据并调用view中方法改变界面显示，
 * 其调用时机是在Fragment类的onResume方法中
 */

public interface BasePresenter {

    void start();

}
