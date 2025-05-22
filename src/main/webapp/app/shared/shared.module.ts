import { NgModule } from '@angular/core';

import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { FilterComponent } from './filter/filter.component';
import { SafeHtmlPipe } from './pipes/safe-html.pipe';
import { NewlineToBrPipe } from './pipes/newline-to-br.pipe';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SafeUrlPipe } from './pipes/safe-url.pipe';

@NgModule({
  imports: [
    SharedLibsModule,
    FontAwesomeModule,
  ],
  declarations: [
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    FilterComponent,
    SafeHtmlPipe,
    NewlineToBrPipe,
    SafeUrlPipe,

  ],
  exports: [
    SharedLibsModule,
    FontAwesomeModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    FilterComponent,
    SafeHtmlPipe,
    NewlineToBrPipe,
    SafeUrlPipe,
  ],
})
export class SharedModule {}
