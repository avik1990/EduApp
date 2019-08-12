package com.app.eduapp.retrofit.api;

import com.app.eduapp.pojo.AllLeaveApplicationListEMP;
import com.app.eduapp.pojo.ApproveDisapprove;
import com.app.eduapp.pojo.ClassRoutineByParent;
import com.app.eduapp.pojo.DiaryDetails;
import com.app.eduapp.pojo.EmpLeaveApplicationList;
import com.app.eduapp.pojo.EmployeeProfile;
import com.app.eduapp.pojo.EventsByDate;
import com.app.eduapp.pojo.ExamListBean;
import com.app.eduapp.pojo.ExamResultListBean;
import com.app.eduapp.pojo.ExamSubjects;
import com.app.eduapp.pojo.FeesReports;
import com.app.eduapp.pojo.FileUploadResponse;
import com.app.eduapp.pojo.GetEmployeeListBySubjectsClass;
import com.app.eduapp.pojo.GetMonthlyAttendance;
import com.app.eduapp.pojo.GetOrPostDiaryAcknowledgement;
import com.app.eduapp.pojo.GetOrPostNoticeAcknowledgement;
import com.app.eduapp.pojo.GetParentsProfile;
import com.app.eduapp.pojo.GetStudentsList;
import com.app.eduapp.pojo.LeaveApplicationListEMP;
import com.app.eduapp.pojo.LeaveDetails;
import com.app.eduapp.pojo.Months;
import com.app.eduapp.pojo.MyLeaveApplicationListEMP;
import com.app.eduapp.pojo.NCommentBean;
import com.app.eduapp.pojo.Notice;
import com.app.eduapp.pojo.NoticeDetailsP;
import com.app.eduapp.pojo.ProfileImageUploadResponse;
import com.app.eduapp.pojo.ResultSubjectsList;
import com.app.eduapp.pojo.SCommentBean;
import com.app.eduapp.pojo.SchoolDetails;
import com.app.eduapp.pojo.StudentDiary;
import com.app.eduapp.pojo.StudentLeave;
import com.app.eduapp.pojo.StudentPostList;
import com.app.eduapp.pojo.StudentsListByClassSectionEMP;
import com.app.eduapp.pojo.TDiaryClass;
import com.app.eduapp.pojo.TDiarySubject;
import com.app.eduapp.pojo.TeacherRoutine;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("getMonthsList")
    Call<Months> getMonthLists();

    @GET("GetOrPostDiaryCommnets")
    Call<SCommentBean> GetOrPostDiaryCommnets(@Query("DiaryPostID") String DiaryPostID);

    @GET("GetOrPostNoticeCommnets")
    Call<NCommentBean> GetOrPostNoticeCommnets(@Query("NoticeID") String NoticeID);

    @GET("getMonthsList")
    Call<GetParentsProfile> getProfile(@Query("ParentsID") String ParentsID, @Query("DeviceID") String DeviceID, @Query("DeviceToken") String DeviceToken);

    @GET("getEmployeeProfile")
    Call<EmployeeProfile> getEmployeeProfile(@Query("EmployeeID") String EmployeeID, @Query("DeviceID") String DeviceID, @Query("DeviceToken") String DeviceToken);

    @GET("getStudentsList")
    Call<GetStudentsList> getStudentsList(@Query("ParentsID") String ParentsID);

    @GET("getMonthlyAttendance")
    Call<GetMonthlyAttendance> getMonthlyAttendance(@Query("StudentsID") String StudentsID, @Query("CurrentYearMonth") String CurrentYearMonth);

    @GET("getNoticeList")
    Call<Notice> getNoticeList(@Query("ParentsID") String ParentsID, @Query("StartDate") String StartDate, @Query("EndDate") String EndDate);

    @GET("getNoticeListEMP")
    Call<Notice> getNoticeListEMP(@Query("EmployeeID") String EmployeeID, @Query("StartDate") String StartDate, @Query("EndDate") String EndDate);

    @GET("getNoticeDetails")
    Call<NoticeDetailsP> getNoticeDetails(@Query("NoticeID") String NoticeID);

    @GET("getMyLeaveList")
    Call<StudentLeave> getMyLeaveList(@Query("StudentsID") String StudentsID);

    @GET("getLeaveApplicationListEMP")
    Call<LeaveApplicationListEMP> getLeaveApplicationListEMP(@Query("EmployeeID") String EmployeeID);

    @GET("getLeaveDetails")
    Call<LeaveDetails> getLeaveDetails(@Query("LeaveID") String LeaveID, @Query("ParentsID") String ParentsID);

    @GET("getLeaveDetails")
    Call<LeaveDetails> getLeaveDetails(@Query("LeaveID") String LeaveID);

    @GET("getDiaryList")
    Call<StudentDiary> getMyDiaryList(@Query("StudentsID") String StudentsID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID);

    @GET("getClassSectionListEMP")
    Call<TDiaryClass> getClassSectionListEMP(@Query("EmployeeID") String EmployeeID);

    @GET("getSubjectsListDiaryEMP")
    Call<TDiarySubject> getSubjectsListDiaryEMP(@Query("EmployeeID") String EmployeeID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID);

    @GET("getDiaryListBySubjects")
    Call<StudentPostList> getStudentPostList(@Query("StudentsID") String StudentsID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID, @Query("SubjectsID") String SubjectsID, @Query("StartDate") String StartDate, @Query("EndDate") String EndDate);

    @GET("getDiaryListBySubjectsEMP")
    Call<StudentPostList> getDiaryListBySubjectsEMP(@Query("EmployeeID") String EmployeeID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID, @Query("SubjectsID") String SubjectsID, @Query("StartDate") String StartDate, @Query("EndDate") String EndDate);

    @GET("getDiaryDetailsEMP")
    Call<DiaryDetails> getDiaryDetailsEMP(@Query("EmployeeID") String EmployeeID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID, @Query("SubjectsID") String SubjectsID, @Query("DiaryPostID") String DiaryPostID);

    @GET("getDiaryDetails")
    Call<DiaryDetails> getDiaryDetails(@Query("StudentsID") String StudentsID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID, @Query("SubjectsID") String SubjectsID, @Query("DiaryPostID") String DiaryPostID);

    @GET("getStudentsListByClassSectionEMP")
    Call<StudentsListByClassSectionEMP> getStudentsListByClassSectionEMP(@Query("ClassID") String ClassID, @Query("SectionID") String SectionID);



    @GET("getExamList")
    Call<ExamListBean> getExamList(@Query("ClassID") String ClassID, @Query("SectionID") String SectionID);

    @GET("getExamSubjectsList")
    Call<ExamSubjects> getExamSubjectsList(@Query("ExamID") String ExamID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID);

    @GET("getResultSubjectsList")
    Call<ResultSubjectsList> getResultSubjectsList(@Query("ExamID") String ExamID, @Query("StudentsID") String StudentsID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID);

    @GET("getExamResultList")
    Call<ExamResultListBean> getExamResultList(@Query("StudentsID") String StudentsID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID);

    @GET("getFeesReportsByStudents")
    Call<FeesReports> getFeesReportsByStudents(@Query("StudentsID") String StudentsID);

    @GET("getMyLeaveApplicationListEMP")
    Call<MyLeaveApplicationListEMP> getMyLeaveApplicationListEMP(@Query("EmployeeID") String EmployeeID);

    @GET("getStudentLeaveApplicationListEMP")
    Call<AllLeaveApplicationListEMP> getStudentLeaveApplicationListEMP(@Query("EmployeeID") String EmployeeID);

    @GET("getEmployeeLeaveApplicationListEMP")
    Call<EmpLeaveApplicationList> getEmployeeLeaveApplicationListEMP(@Query("EmployeeID") String EmployeeID);

    @GET("setApproveDisapprove")
    Call<ApproveDisapprove> setApproveDisapprove(@Query("LeaveID") String LeaveID, @Query("ApprovedById") String ApprovedById, @Query("IsApproved") String IsApproved);

    @GET("deleteDiaryPost")
    Call<ApproveDisapprove> deleteDiaryPost(@Query("EmployeeID") String EmployeeID, @Query("ClassID") String ClassID, @Query("SectionID") String SectionID, @Query("SubjectsID") String SubjectsID, @Query("DiaryPostID") String DiaryPostID);

    @GET("getSchoolDetails")
    Call<SchoolDetails> getSchoolDetails(@Query("SchoolID") String SchoolID);

    @GET("getEventsByDate")
    Call<EventsByDate> getEventsByDate(@Query("CurrentDate") String CurrentDate);

    @GET("getEventsByMonth")
    Call<EventsByDate> getEventsByMonth(@Query("CurrentYearMonth") String CurrentYearMonth);

    @GET("getClassRoutineByParent")
    Call<ClassRoutineByParent> getClassRoutineByParent(@Query("ClassID") String ClassID, @Query("SectionID") String SectionID);

    @GET("getClassRoutineByTeacherEMP")
    Call<TeacherRoutine> getClassRoutineByTeacherEMP(@Query("EmployeeID") String EmployeeID, @Query("ClassScheduleID") String ClassScheduleID);

   /* @GET("GetOrPostDiaryAcknowledgement")
    Call<GetOrPostDiaryAcknowledgement> GetOrPostDiaryAcknowledgement(@Query("DiaryPostID") String DiaryPostID, @Query("StudentsID") String StudentsID);*/

    @GET("GetOrPostDiaryAcknowledgement")
    Call<GetOrPostDiaryAcknowledgement> GetOrPostDiaryAcknowledgement(@Query("DiaryPostID") String DiaryPostID);

    @GET("GetOrPostNoticeAcknowledgement")
    Call<GetOrPostNoticeAcknowledgement> GetOrPostNoticeAcknowledgement(@Query("NoticeID") String NoticeID, @Query("StudentsID") String StudentsID);

    @GET("getEmployeeListBySubjectsClass")
    Call<GetEmployeeListBySubjectsClass> GetEmployeeListBySubjectsClass(@Query("ClassID") String ClassID, @Query("SectionID") String SectionID);

    @GET("deleteLeaveApplication")
    Call<ApproveDisapprove> deleteLeaveApplication(@Query("LeaveID") String LeaveID);


    @Multipart
    @POST("updateStudentsProfilePicture")
    Call<ProfileImageUploadResponse> uploadProfileImage(@Part List<MultipartBody.Part> image, @Query("StudentsID") String StudentsID, @Query("ClassID") String SectionID);

    @Multipart
    @POST("uploadFileInServer")
    Call<FileUploadResponse> uploadImage(@Part List<MultipartBody.Part> image);

}
