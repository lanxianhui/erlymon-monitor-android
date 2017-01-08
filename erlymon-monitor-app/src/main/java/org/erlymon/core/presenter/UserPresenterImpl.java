/*
 * Copyright (c) 2016, Sergey Penkovsky <sergey.penkovsky@gmail.com>
 *
 * This file is part of Erlymon Monitor.
 *
 * Erlymon Monitor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Erlymon Monitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Erlymon Monitor.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.erlymon.core.presenter;


import android.content.Context;
import android.util.Log;

import org.erlymon.core.model.Model;
import org.erlymon.core.model.ModelImpl;
import org.erlymon.core.model.data.StorageModule;
import org.erlymon.core.model.data.User;
import org.erlymon.core.view.UserView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by Sergey Penkovsky <sergey.penkovsky@gmail.com> on 5/4/16.
 */
public class UserPresenterImpl implements UserPresenter {
    private static final Logger logger = LoggerFactory.getLogger(UserPresenterImpl.class);
    private Model model;

    private UserView view;
    private Subscription subscription = Subscriptions.empty();

    public UserPresenterImpl(Context context, UserView view) {
        this.view = view;
        this.model = new ModelImpl(context);
    }

    @Override
    public void onSaveButtonClick() {

        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        if (view.getUserId() == 0) {
            subscription = model.createUser(view.getUser())
                    .flatMap(new Func1<User, Observable<User>>() {
                        @Override
                        public Observable<User> call(User user) {
                            return StorageModule.getInstance().getStorage()
                                    .put()
                                    .object(user)
                                    .prepare()
                                    .asRxObservable()
                                    .map(putResult -> user);
                        }
                    })
                    .doOnError(throwable -> logger.error(Log.getStackTraceString(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            logger.error(Log.getStackTraceString(e));
                            view.showError(e.getMessage());
                        }

                        @Override
                        public void onNext(User data) {
                            view.showData(data);
                        }
                    });
        } else {
            subscription = model.updateUser(view.getUserId(), view.getUser())
                    .flatMap(new Func1<User, Observable<User>>() {
                        @Override
                        public Observable<User> call(User user) {
                            return StorageModule.getInstance().getStorage()
                                    .put()
                                    .object(user)
                                    .prepare()
                                    .asRxObservable()
                                    .map(putResult -> user);
                        }
                    })
                    .doOnError(throwable -> logger.error(Log.getStackTraceString(throwable)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            logger.error(Log.getStackTraceString(e));
                            view.showError(e.getMessage());
                        }

                        @Override
                        public void onNext(User data) {
                            view.showData(data);
                        }
                    });
        }
    }

    @Override
    public void onStop() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
