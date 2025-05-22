import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EnterpriseFormService, EnterpriseFormGroup } from './enterprise-form.service';
import { IEnterprise } from '../enterprise.model';
import { EnterpriseService } from '../service/enterprise.service';
import { Country } from 'app/entities/enumerations/country.model';
import { Currency } from 'app/entities/enumerations/currency.model';

@Component({
  selector: 'jhi-enterprise-update',
  templateUrl: './enterprise-update.component.html',
})
export class EnterpriseUpdateComponent implements OnInit {
  isSaving = false;
  enterprise: IEnterprise | null = null;
  countryValues = Object.keys(Country);
  currencyValues = Object.keys(Currency);

  editForm: EnterpriseFormGroup = this.enterpriseFormService.createEnterpriseFormGroup();

  constructor(
    protected enterpriseService: EnterpriseService,
    protected enterpriseFormService: EnterpriseFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enterprise }) => {
      this.enterprise = enterprise;
      if (enterprise) {
        this.updateForm(enterprise);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enterprise = this.enterpriseFormService.getEnterprise(this.editForm);
    if (enterprise.id !== null) {
      this.subscribeToSaveResponse(this.enterpriseService.update(enterprise));
    } else {
      this.subscribeToSaveResponse(this.enterpriseService.create(enterprise));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnterprise>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(enterprise: IEnterprise): void {
    this.enterprise = enterprise;
    this.enterpriseFormService.resetForm(this.editForm, enterprise);
  }
}
