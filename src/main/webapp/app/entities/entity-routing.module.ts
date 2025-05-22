import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'business-plan',
        data: { pageTitle: 'readyToPlanBeApp.businessPlan.home.title' },
        loadChildren: () => import('./business-plan/business-plan.module').then(m => m.BusinessPlanModule),
      },
      {
        path: 'enterprise',
        data: { pageTitle: 'readyToPlanBeApp.enterprise.home.title' },
        loadChildren: () => import('./enterprise/enterprise.module').then(m => m.EnterpriseModule),
      },
      {
        path: 'manual-business-plan',
        data: { pageTitle: 'readyToPlanBeApp.manualBusinessPlan.home.title' },
        loadChildren: () => import('./manual-business-plan/manual-business-plan.module').then(m => m.ManualBusinessPlanModule),
      },
      {
        path: 'financial-forecast',
        data: { pageTitle: 'readyToPlanBeApp.financialForecast.home.title' },
        loadChildren: () => import('./financial-forecast/financial-forecast.module').then(m => m.FinancialForecastModule),
      },
      {
        path: 'product-or-service',
        data: { pageTitle: 'readyToPlanBeApp.productOrService.home.title' },
        loadChildren: () => import('./product-or-service/product-or-service.module').then(m => m.ProductOrServiceModule),
      },
      {
        path: 'revenue-forecast',
        data: { pageTitle: 'readyToPlanBeApp.revenueForecast.home.title' },
        loadChildren: () => import('./revenue-forecast/revenue-forecast.module').then(m => m.RevenueForecastModule),
      },
      {
        path: 'expense-forecast',
        data: { pageTitle: 'readyToPlanBeApp.expenseForecast.home.title' },
        loadChildren: () => import('./expense-forecast/expense-forecast.module').then(m => m.ExpenseForecastModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
