import { FishingLesson } from '../model/fishing-lesson';
import { FishingLessonReservation } from '../model/fishing-lesson-reservation';
import { Instructor } from '../model/instructor';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Credentials } from '../model/credentials';
import { Observable, throwError } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';
import { TermFishingLesson } from '../model/term-fishing-lesson';
import { FishingLessonReservationWithDateAsString } from '../model/fishing-lesson-reservation-with-date-as-string';
import { FishingLessonFeedback } from '../model/fishing-lesson-feedback';
import { FishingLessonComplaint } from '../model/fishing-lesson-complaint';


@Injectable({
  providedIn: 'root'
})
export class InstructorService {
  
  // private _baseUrl = 'http://localhost:8080';  
  private _instructorController ='/api/instructors';
  private _fishingLessonReservationController ='/api/fishingLessonReservations';
  private _submitRegistration  = this._instructorController + '/create';
  private _allBoats  = this._instructorController + '/allFishingLessons';
  private _getAllFreeTermsForBoat = this._instructorController + '/getAllFreeTermsForFishingLesson/';
  private _findAvailableBoats  = this._instructorController + '/findAvailableForDateRange';
  private _makeReservation = this._fishingLessonReservationController + '/reserve';
  private _cancelReservation = this._fishingLessonReservationController + '/cancel/';
  private _getFutureReservationsForCustomerUsername = this._fishingLessonReservationController + '/getFutureForCustomerUsername/';
  private _getPastReservationsForCustomerUsername = this._fishingLessonReservationController + '/getPastForCustomerUsername/';
  private _sendFeedback = this._fishingLessonReservationController + '/sendFeedback';
  private _sendComplaint = this._fishingLessonReservationController + '/sendComplaint';


  constructor(private _http: HttpClient) { }


  sendComplaint(complaint: FishingLessonComplaint) : Observable<any> {
    const body=JSON.stringify(complaint);
    console.log(body)
    return this._http.post(this._sendComplaint, body)
  }
  sendFeedback(feedback: FishingLessonFeedback) : Observable<any> {
    const body=JSON.stringify(feedback);
    console.log(body)
    return this._http.post(this._sendFeedback, body)
  }
  getPastReservationsForCustomerUsername(username: string) : Observable<FishingLessonReservationWithDateAsString[]> {
    return this._http.get<FishingLessonReservationWithDateAsString[]>(this._getPastReservationsForCustomerUsername + username)
                      .pipe(tap(data =>  console.log('Iz service-a: ', data)),                         
                      catchError(this.handleError)); 
  }

  cancelReservation(id: number) {
    return this._http.post(this._cancelReservation + id, {})
  }
  getFutureReservationsForCustomerUsername(username: string) {
    return this._http.get<FishingLessonReservationWithDateAsString[]>(this._getFutureReservationsForCustomerUsername + username)
                      .pipe(tap(data =>  console.log('Iz service-a: ', data)),                         
                      catchError(this.handleError)); 
  }

  createInstructor(instructor: Instructor) : Observable<any> {
    const body=JSON.stringify(instructor);
    console.log(body)
    return this._http.post(this._submitRegistration, body)
  } 

  reserve(fishingReservation: FishingLessonReservation): Observable<any> {
    const body=JSON.stringify(fishingReservation);
    console.log(body)
    return this._http.post(this._makeReservation, body)
  }

  findAvailableFishingLessonsForSelectedTerm(start: any, end: any) : Observable<any> {
    start = new Date(Date.UTC(start.getFullYear(), start.getMonth(), start.getDate(), start.getHours(), start.getMinutes()));
    end = new Date(Date.UTC(end.getFullYear(), end.getMonth(), end.getDate(), end.getHours(), end.getMinutes()));
    const body=JSON.stringify({start, end});
    console.log(body)
    return this._http.post(this._findAvailableBoats, body)
  }

  getAllFreeTermsForFishingLesson(selectedFishingLessonInfo: FishingLesson) :Observable<TermFishingLesson[]> {
    return this._http.get<TermFishingLesson[]>(this._getAllFreeTermsForBoat + selectedFishingLessonInfo.id)
                          .pipe(tap(data =>  console.log('All: ' + JSON.stringify(data))),
                            catchError(this.handleError)); 
  }

  getAllFishingLessons(): Observable<FishingLesson[]> {
    return this._http.get<FishingLesson[]>(this._allBoats)
                      .pipe(tap(data =>  console.log('Iz service-a: ', data)),                         
                      catchError(this.handleError)); 
  }



  private handleError(err : HttpErrorResponse) {
    console.log(err.message);
    return Observable.throw(err.message);
    throw new Error('Method not implemented.');
  } 
}
