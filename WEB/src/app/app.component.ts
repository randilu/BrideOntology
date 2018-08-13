import {Component} from '@angular/core';
import {QuestionService} from './app.service';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private questionService: QuestionService,private spinnerService: Ng4LoadingSpinnerService) {

  }

  lenght: number;
  brides: any[] = [{}, {}, {}, {}, {},{}, {}, {}, {}, {}];
  bridesFinal: any[] = [];
  bride: any = {};
  title = 'Brides';
  selectedCast: string;
  selectedReligion: string;
  selectedRace: string;
  resultsGot: any[] = [];
  gotResults = false;
  castes: string[] = ['No_caste', 'Govigama', 'Bathgama', 'Karawa', 'Durawa', 'Salagama', 'Berava', 'Deva', 'Kinnara', 'Rada'];
  religions: string[] = ['Buddhist', 'Christian', 'Catholic'];
  races: string[] = ['Sinhala', 'Burgher', 'Tamil'];
  categories: string[] = ['Excellent_colombo_bride', 'Colombo_bride', 'Normal_colombo_bride', 'Normal_Kandyan_bride', 'Excellent_kandyan_bride', 'Kandyan_bride',  'Excellent_southern_bride', 'Bride_with_Occupation', 'Educated_bride', 'southern_bride', 'Normal_southern_bride' ];

  selectedBrideInfo: any[] = [];
  individulaSelected = false;
  recommenedBrides: any[] = [];
  selectedCategory = '';
  loading = false;
  str;
  predictions = [];
  recommendationsOn = false;
  categorySelected = false;

  setCaste(caste: string) {
    this.selectedCast = caste;
  }

  setReligion(religion: string) {
    this.selectedReligion = religion;
  }

  setRace(race: string) {
    this.selectedRace = race;
  }

  onClickGetQuestions() {
    this.questionService.getQuestions(this.str).subscribe(
      (res) => {
        this.predictions = res;
        console.log(this.predictions);
      },
      () => {

      }
    );
  }

  onEntertGetQuestions(event) {
    this.onClickGetQuestions();
  }

  onPaste(event) {
    this.loading = true;
    console.log(event.clipboardData.getData('text/plain'));
    this.questionService.getQuestions(event.clipboardData.getData('text/plain')).subscribe(
      (res) => {
        this.loading = false;
        this.predictions = res;
        console.log(this.predictions);
      },
      () => {
        this.loading = false;
      }
    );
  }

  onClickGetCombination() {

    this.questionService.getFromCombination(this.selectedCast || 'empty', this.selectedReligion || 'empty', this.selectedRace || 'empty').subscribe(
      (res) => {
        this.resultsGot = res;
        if (this.resultsGot.length !== 0) {
          console.log(this.resultsGot.length);
          this.gotResults = true;
          this.individulaSelected = false;
          this.recommendationsOn = false;
        }
        console.log(res);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  brideSelected(brideName: string) {
    this.questionService.getBrideInfo(brideName).subscribe(
      (res) => {
        this.individulaSelected = true;
        this.selectedBrideInfo = res;
        this.selectedBrideInfo.forEach(
          (item) => {
            switch (item.predicate) {
              case 'hasCaste': {
                this.bride.hasCaste = item.object;
                break;
              }
              case 'URL': {
                this.bride.url = item.object;
                break;
              }
              case 'livesIn': {
                this.bride.livesIn = item.object;
                break;
              }
              case 'age': {
                this.bride.age = item.object;
                break;
              }
              case 'hasReligion': {
                this.bride.hasReligion = item.object;
                break;
              }
              case 'hasOccupation': {
                this.bride.hasOccupation = item.object;
                break;
              }
              case 'hasSchool': {
                this.bride.hasSchool = item.object;
                break;
              }
              case 'label': {
                this.bride.label = item.object;
                break;
              }
              case 'hasDegree': {
                this.bride.hasDegree = item.object;
                break;
              }
              case 'type': {
                this.bride.type = item.object;
                break;
              }
              case 'height': {
                this.bride.height = item.object;
                break;
              }
              case 'hasSkinTone': {
                this.bride.hasSkinTone = item.object;
                break;
              }
              case 'hasDegree': {
                this.bride.hasDegree = item.object;
                break;
              }
              case 'hasRace': {
                this.bride.hasRace = item.object;
                break;
              }
              case 'hasMaritalStatus': {
                this.bride.hasMaritalStatus = item.object;
                break;
              }
            }
            console.log(this.bride);
          }
        )
        ;
        console.log(this.selectedBrideInfo);
      },
      (err) => {

      }
    );


  }

  tryRecommendationClicked() {
    this.recommendationsOn = true;

  }

  selectCategory(category: string) {
    this.bridesFinal = [];
    this.spinnerService.show();
    console.log('request sent');
    this.selectedCategory = category;
    this.questionService.getInferred(category).subscribe(
      (res) => {
        console.log('response recieved');
        this.categorySelected = true;
        this.individulaSelected = true;
        this.recommenedBrides = res;
        this.spinnerService.hide();
        this.recommenedBrides.forEach(
          (item) => {
            switch (item.predicate) {
              case 'hasCaste': {
                this.brides[item.index].hasCaste = item.object;
                break;
              }
              case 'URL': {
                this.brides[item.index].url = item.object;
                break;
              }
              case 'livesIn': {
                this.brides[item.index].livesIn = item.object;
                break;
              }
              case 'age': {
                this.brides[item.index].age = item.object;
                break;
              }
              case 'hasReligion': {
                this.brides[item.index].hasReligion = item.object;
                break;
              }
              case 'hasOccupation': {
                this.brides[item.index].hasOccupation = item.object;
                break;
              }
              case 'hasSchool': {
                this.brides[item.index].hasSchool = item.object;
                break;
              }
              case 'label': {
                this.brides[item.index].label = item.object;
                break;
              }
              case 'hasDegree': {
                this.brides[item.index].hasDegree = item.object;
                break;
              }
              case 'type': {
                this.brides[item.index].type = item.object;
                break;
              }
              case 'height': {
                this.brides[item.index].height = item.object;
                break;
              }
              case 'hasSkinTone': {
                this.brides[item.index].hasSkinTone = item.object;
                break;
              }
              case 'hasDegree': {
                this.brides[item.index].hasDegree = item.object;
                break;
              }
              case 'hasRace': {
                this.brides[item.index].hasRace = item.object;
                break;
              }
              case 'hasMaritalStatus': {
                this.brides[item.index].hasMaritalStatus = item.object;
                break;
              }
            }

          }
        );
        // console.log(this.brides);
        this.brides.forEach(
          (bride) => {
            if(bride.hasOwnProperty('height', 'height')) {
              this.bridesFinal.push(bride);
            }
          }
        );

        console.log(this.bridesFinal);
      }, (err) => {

      }
    );
  }
}
