import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

@Injectable()

export class QuestionService {


  constructor(private http: HttpClient) {

  }

  getQuestions(str: string) {
    let data = {str: str};
    return this.http.post<any>('https://f3x12oflx2.execute-api.ap-south-1.amazonaws.com/dev/selectQuery', data);
  }

  getFromCombination(caste: string, religion: string, race: string) {
    return this.http.get<any>(`http://bride-x.us-east-1.elasticbeanstalk.com/rest/json/getFromCombination/${caste}/${religion}/${race}`);
  }
  getBrideInfo(brideName: string){
    return this.http.get<any>(`http://bride-x.us-east-1.elasticbeanstalk.com/rest/json/getAll/${brideName}`);
  }
  getInferred(category: string){
    return  this.http.get<any>(`http://bride-x.us-east-1.elasticbeanstalk.com/rest/json/inferred/${category}`);
  }
}
