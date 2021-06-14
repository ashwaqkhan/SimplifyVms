import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BasicDetailsComponent } from './list/basic-details.component';
import { BasicDetailsDetailComponent } from './detail/basic-details-detail.component';
import { BasicDetailsUpdateComponent } from './update/basic-details-update.component';
import { BasicDetailsDeleteDialogComponent } from './delete/basic-details-delete-dialog.component';
import { BasicDetailsRoutingModule } from './route/basic-details-routing.module';

@NgModule({
  imports: [SharedModule, BasicDetailsRoutingModule],
  declarations: [BasicDetailsComponent, BasicDetailsDetailComponent, BasicDetailsUpdateComponent, BasicDetailsDeleteDialogComponent],
  entryComponents: [BasicDetailsDeleteDialogComponent],
})
export class BasicDetailsModule {}
