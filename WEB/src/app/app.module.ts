import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { QuestionService } from "./app.service";
import { HttpClientModule } from "@angular/common/http";
import { FormsModule} from "@angular/forms";
import { Ng4LoadingSpinnerModule } from 'ng4-loading-spinner';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent,

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    Ng4LoadingSpinnerModule.forRoot()
  ],
  providers: [
    HttpClient,
    QuestionService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
