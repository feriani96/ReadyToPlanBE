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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
