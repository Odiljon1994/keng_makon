package com.furniture.kengmakon.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.furniture.kengmakon.ui.viewmodels.ActionsVM;
import com.furniture.kengmakon.ui.viewmodels.AuthVM;
import com.furniture.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.furniture.kengmakon.ui.viewmodels.UserVM;
import com.furniture.kengmakon.ui.viewmodels.EditAccountVM;
import com.furniture.kengmakon.ui.viewmodels.FurnitureVM;
import com.furniture.kengmakon.ui.viewmodels.InfoVM;
import com.furniture.kengmakon.ui.viewmodels.NotificationsVM;
import com.furniture.kengmakon.ui.viewmodels.OrdersVM;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(AuthVM.class)
    abstract ViewModel bindAuthVm(AuthVM currentFeeVM);

    @Binds
    @IntoMap
    @ViewModelKey(FurnitureVM.class)
    abstract ViewModel bindFurnitureVm(FurnitureVM furnitureVM);

    @Binds
    @IntoMap
    @ViewModelKey(FurnitureDetailsVM.class)
    abstract ViewModel bindFurnitureDetailsVm(FurnitureDetailsVM furnitureDetailsVM);

    @Binds
    @IntoMap
    @ViewModelKey(UserVM.class)
    abstract ViewModel bindUserVm(UserVM furnitureDetailsVM);

    @Binds
    @IntoMap
    @ViewModelKey(OrdersVM.class)
    abstract ViewModel bindOrdersVm(OrdersVM ordersVM);

    @Binds
    @IntoMap
    @ViewModelKey(InfoVM.class)
    abstract ViewModel bindInfoVm(InfoVM ordersVM);

    @Binds
    @IntoMap
    @ViewModelKey(ActionsVM.class)
    abstract ViewModel bindActionsVm(ActionsVM ordersVM);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsVM.class)
    abstract ViewModel bindNotificationsVm(NotificationsVM notificationsVM);

    @Binds
    @IntoMap
    @ViewModelKey(EditAccountVM.class)
    abstract ViewModel bindEditAccountVm(EditAccountVM editAccountVM);
}
