import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {SignupComponent} from './auth/signup/signup.component';
import {AppRoutingModule} from './app-routing.module';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { LoginComponent } from './auth/login/login.component';
import {NgxWebstorageModule} from "ngx-webstorage";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";
import { AdminComponent } from './admin/admin.component';
import { UserComponent } from './user/user.component';
import {TokenInterceptor} from "./token-interceptor";
import { TaskTileComponent } from './shared/task-tile/task-tile.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatInputModule} from "@angular/material/input";
import {
    NGX_MAT_DATE_FORMATS, NgxMatDateAdapter, NgxMatDateFormats,
    NgxMatDatetimePickerModule,
    NgxMatNativeDateModule,
    NgxMatTimepickerModule
} from "@angular-material-components/datetime-picker";
import {MatButtonModule} from "@angular/material/button";
import {MatRadioModule} from "@angular/material/radio";
import {MatSelectModule} from "@angular/material/select";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {NgxMatMomentModule} from "@angular-material-components/moment-adapter";
import {MatIconModule} from "@angular/material/icon";

const CUSTOM_DATE_FORMATS: NgxMatDateFormats = {
    parse: {
        dateInput: 'l, LTS'
    },
    display: {
        dateInput: 'DD-MM-YYYY HH:mm',
        monthYearLabel: 'MMM YYYY',
        dateA11yLabel: 'LL',
        monthYearA11yLabel: 'MMMM YYYY',
    }
};

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        SignupComponent,
        LoginComponent,
        AdminComponent,
        UserComponent,
        TaskTileComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
        NgxWebstorageModule.forRoot(),
        BrowserAnimationsModule,
        ToastrModule.forRoot(),
        DragDropModule,
        FontAwesomeModule,
        FormsModule,

        MatDatepickerModule,
        MatInputModule,
        NgxMatDatetimePickerModule,
        NgxMatTimepickerModule,
        MatButtonModule,

        MatRadioModule,
        MatSelectModule,
        MatCheckboxModule,

        NgxMatNativeDateModule,
        NgxMatMomentModule,
        MatIconModule,

    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenInterceptor,
            multi: true
        },
        {
            provide: NGX_MAT_DATE_FORMATS,
            useValue: CUSTOM_DATE_FORMATS
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
