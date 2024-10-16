package com.toplevel.kengmakon.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.toplevel.kengmakon.ui.viewmodels.ActionsVM;
import com.toplevel.kengmakon.ui.viewmodels.AuthVM;
import com.toplevel.kengmakon.ui.viewmodels.EditAccountVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureDetailsVM;
import com.toplevel.kengmakon.ui.viewmodels.FurnitureVM;
import com.toplevel.kengmakon.ui.viewmodels.InfoVM;
import com.toplevel.kengmakon.ui.viewmodels.NotificationsVM;
import com.toplevel.kengmakon.ui.viewmodels.OrdersVM;
import com.toplevel.kengmakon.ui.viewmodels.UserVM;

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
