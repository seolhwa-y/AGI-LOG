-- TABLE

CREATE TABLE "SOCIALUSER" (
	"SU_CODE"	NVARCHAR2(50)		NOT NULL,
	"SU_NAME"	NVARCHAR2(500)		NOT NULL,
	"SU_PHONE"	NVARCHAR2(500)		NOT NULL,
	"SU_EMAIL"	NVARCHAR2(500)		NOT NULL,
	"SU_NICKNAME"	NVARCHAR2(20)		NOT NULL
);

CREATE TABLE "CATEGORY" (
	"CA_CODE"	NCHAR(2)		NOT NULL,
	"CA_NAME"	NVARCHAR2(20)		NOT NULL,
	"CA_TYPE"	NVARCHAR2(5)		NOT NULL
);

CREATE TABLE "COMPANY" (
	"CO_CODE"	NCHAR(10)		NOT NULL,
	"CO_PASSWORD"	NVARCHAR2(500)		NOT NULL,
	"CO_MANAGERCODE"	NVARCHAR2(500)		NOT NULL,
	"CO_NAME"	NVARCHAR2(500)		NOT NULL,
	"CO_PHONE"	NVARCHAR2(500)		NOT NULL,
	"CO_EMAIL"	NVARCHAR2(500)		NOT NULL,
	"CO_ADDRESS"	NVARCHAR2(100)		NOT NULL
);

CREATE TABLE "AUTHLOG" (
	"AL_DATE"	DATE		NOT NULL,
	"AL_ACTION"	NUMBER(1)		NOT NULL,
	"AL_SUCODE"	NVARCHAR2(50)	DEFAULT SYSDATE	NOT NULL
);

CREATE TABLE "COMPANYLOG" (
	"CL_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"CL_ACTION"	NUMBER(1)		NOT NULL,
	"CL_COCODE"	NCHAR(10)		NOT NULL
);

CREATE TABLE "BABY" (
	"BB_CODE"	NCHAR(2)		NOT NULL,
	"BB_SUCODE"	NVARCHAR2(50)		NOT NULL,
	"BB_NAME"	NVARCHAR2(20)		NOT NULL,
	"BB_BIRTHDAY"	DATE		NOT NULL
);

CREATE TABLE "MAPCOMMENT" (
	"MC_CODE"	NVARCHAR2(10)		NOT NULL,
	"MC_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"MC_COCODE"	NCHAR(10)		NOT NULL,
	"MC_SUCODE"	NVARCHAR2(50)		NOT NULL,
	"MC_CONTENT"	NVARCHAR2(100)		NOT NULL
);

CREATE TABLE "RESERVATION" (
	"RE_CODE"	NVARCHAR2(10)		NOT NULL,
	"RE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"RE_ACCESS"	NCHAR(1)		NOT NULL,
	"RE_DOCOCODE"	NCHAR(10)		NOT NULL,
	"RE_DOCODE"	NVARCHAR2(20)		NOT NULL,
	"RE_RCCODE"	NCHAR(2)		NOT NULL,
	"RE_BBSUCODE"	NVARCHAR2(50)		NOT NULL,
	"RE_BBCODE"	NCHAR(2)		NOT NULL,
	"RE_COMMENT"	NVARCHAR2(1000)		NULL
);

CREATE TABLE "HASHTAG" (
	"HT_DDCODE"	NVARCHAR2(10)		NOT NULL,
	"HT_DDSUCODE"	NVARCHAR2(50)		NOT NULL,
	"HT_DDDATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"HT_NAME"	NVARCHAR2(20)		NOT NULL
);

CREATE TABLE "DAILYDIARY" (
	"DD_CODE"	NVARCHAR2(10)		NOT NULL,
	"DD_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"DD_SUCODE"	NVARCHAR2(50)		NOT NULL,
	"DD_STASUS"	NCHAR(1)		NOT NULL,
	"DD_CONTENT"	NVARCHAR2(1000)		NOT NULL,
	"DD_VIEW"	NUMBER(10)		NOT NULL,
	"DD_LIKE"	NUMBER(10)		NULL
);

CREATE TABLE "DAILYDIARYPHOTO" (
	"DP_CODE"	NVARCHAR2(100)		NOT NULL,
	"DP_DDCODE"	NVARCHAR2(10)		NOT NULL,
	"DP_DDSUCODE"	NVARCHAR2(50)		NOT NULL,
	"DP_DDDATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"DP_LINK"	NVARCHAR2(500)		NOT NULL
);

CREATE TABLE "DAILYDIARYCOMMENT" (
	"DC_CODE"	NVARCHAR2(10)		NOT NULL,
	"DC_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"DC_DDCODE"	NVARCHAR2(10)		NOT NULL,
	"DC_DDSUCODE"	NVARCHAR2(50)		NOT NULL,
	"DC_DDDATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"DC_SUCODE"	NVARCHAR2(50)		NOT NULL,
	"DC_CONTENT"	NVARCHAR2(1000)		NOT NULL
);

CREATE TABLE "FREEBOARD" (
	"FB_CODE"	NVARCHAR2(10)		NOT NULL,
	"FB_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"FB_SUCODE"	NVARCHAR2(50)		NOT NULL,
	"FB_TITLE"	NVARCHAR2(100)		NOT NULL,
	"FB_CONTENT"	NVARCHAR2(5000)		NOT NULL,
	"FB_VIEW"	NUMBER(10)		NOT NULL,
	"FB_LIKE"	NUMBER(10)		NULL
);

CREATE TABLE "FREEBOARDCOMMENT" (
	"FC_CODE"	NVARCHAR2(10)		NOT NULL,
	"FC_FBCODE"	NVARCHAR2(10)		NOT NULL,
	"FC_FBSUCODE"	NVARCHAR2(50)		NOT NULL,
	"FC_FBDATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"FC_SUCODE"	NVARCHAR2(50)		NOT NULL,
	"FC_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"FC_CONTENT"	NVARCHAR2(1000)		NOT NULL
);

CREATE TABLE "FREEBOARDPHOTO" (
	"FP_CODE"	NUMBER(2)		NOT NULL,
	"FP_FBCODE"	NVARCHAR2(10)		NOT NULL,
	"FP_FBSUCODE"	NVARCHAR2(50)		NOT NULL,
	"FP_FBDATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"FP_LINK"	NVARCHAR2(500)		NOT NULL
);

CREATE TABLE "INFOBOARD" (
	"IB_CODE"	NVARCHAR2(10)		NOT NULL,
	"IB_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"IB_TITLE"	NVARCHAR2(100)		NOT NULL,
	"IB_CONTENT"	NVARCHAR2(5000)		NOT NULL,
	"IB_VIEW"	NUMBER(10)		NOT NULL,
	"IB_LIKE"	NUMBER(10)		NULL
);

CREATE TABLE "SCHEDULE" (
	"SC_CODE"	NVARCHAR2(10)		NOT NULL,
	"SC_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"SC_SUCODE"	NVARCHAR2(50)		NOT NULL,
	"SC_CONTENT"	NVARCHAR2(100)		NOT NULL
);

CREATE TABLE "HEALTHDIARY" (
	"HD_CODE"	NVARCHAR2(10)		NOT NULL,
	"HD_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"HD_CACODE"	NCHAR(2)		NOT NULL,
	"HD_BBSUCODE"	NVARCHAR2(50)		NOT NULL,
	"HD_BBCODE"	NCHAR(2)		NOT NULL,
	"HD_VALUE"	NVARCHAR2(100)		NOT NULL
);

CREATE TABLE "DOCTOR" (
	"DO_CODE"	NVARCHAR2(20)		NOT NULL,
	"DO_COCODE"	NCHAR(10)		NOT NULL,
	"DO_NAME"	NVARCHAR2(500)		NOT NULL,
	"DO_PASSWORD"	NVARCHAR2(500)		NOT NULL
);

CREATE TABLE "RESERVATIONCATEGORY" (
	"RC_CODE"	NCHAR(2)		NOT NULL,
	"RC_NAME"	NVARCHAR2(20)		NOT NULL
);

CREATE TABLE "FREEBOARDLIKE" (
	"FL_FBCODE"	NVARCHAR2(10)		NOT NULL,
	"FL_FBSUCODE"	NVARCHAR2(50)		NOT NULL,
	"FL_FBDATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"FL_SUCODE"	NVARCHAR2(50)		NOT NULL
);

CREATE TABLE "DAILYDIARYLIKE" (
	"DL_DDCODE"	NVARCHAR2(10)		NOT NULL,
	"DL_DDSUCODE"	NVARCHAR2(50)		NOT NULL,
	"DL_DDDATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"DL_SUCODE"	NVARCHAR2(50)		NOT NULL
);

CREATE TABLE "INFOBOARDLIKE" (
	"IL_IBCODE"	NVARCHAR2(10)		NOT NULL,
	"IL_IBDATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"IL_SUCODE"	NVARCHAR2(50)		NOT NULL
);









-- CONSTRAINT : PK


ALTER TABLE "SOCIALUSER" ADD CONSTRAINT "PK_SOCIALUSER" PRIMARY KEY (
	"SU_CODE"
);

ALTER TABLE "CATEGORY" ADD CONSTRAINT "PK_CATEGORY" PRIMARY KEY (
	"CA_CODE"
);

ALTER TABLE "COMPANY" ADD CONSTRAINT "PK_COMPANY" PRIMARY KEY (
	"CO_CODE"
);

ALTER TABLE "AUTHLOG" ADD CONSTRAINT "PK_AUTHLOG" PRIMARY KEY (
	"AL_DATE",
	"AL_ACTION",
	"AL_SUCODE"
);

ALTER TABLE "COMPANYLOG" ADD CONSTRAINT "PK_COMPANYLOG" PRIMARY KEY (
	"CL_DATE",
	"CL_ACTION",
	"CL_COCODE"
);

ALTER TABLE "BABY" ADD CONSTRAINT "PK_BABY" PRIMARY KEY (
	"BB_CODE",
	"BB_SUCODE"
);

ALTER TABLE "MAPCOMMENT" ADD CONSTRAINT "PK_MAPCOMMENT" PRIMARY KEY (
	"MC_CODE",
	"MC_DATE",
	"MC_COCODE",
	"MC_SUCODE"
);

ALTER TABLE "RESERVATION" ADD CONSTRAINT "PK_RESERVATION" PRIMARY KEY (
	"RE_CODE",
	"RE_DATE",
	"RE_ACCESS",
	"RE_DOCOCODE",
	"RE_DOCODE",
	"RE_RCCODE",
	"RE_BBSUCODE",
	"RE_BBCODE"
);

ALTER TABLE "HASHTAG" ADD CONSTRAINT "PK_HASHTAG" PRIMARY KEY (
	"HT_DDCODE",
	"HT_DDSUCODE",
	"HT_DDDATE"
);

ALTER TABLE "DAILYDIARY" ADD CONSTRAINT "PK_DAILYDIARY" PRIMARY KEY (
	"DD_CODE",
	"DD_DATE",
	"DD_SUCODE"
);

ALTER TABLE "DAILYDIARYPHOTO" ADD CONSTRAINT "PK_DAILYDIARYPHOTO" PRIMARY KEY (
	"DP_CODE",
	"DP_DDCODE",
	"DP_DDSUCODE",
	"DP_DDDATE"
);

ALTER TABLE "DAILYDIARYCOMMENT" ADD CONSTRAINT "PK_DAILYDIARYCOMMENT" PRIMARY KEY (
	"DC_CODE",
	"DC_DATE",
	"DC_DDCODE",
	"DC_DDSUCODE",
	"DC_DDDATE",
	"DC_SUCODE"
);

ALTER TABLE "FREEBOARD" ADD CONSTRAINT "PK_FREEBOARD" PRIMARY KEY (
	"FB_CODE",
	"FB_DATE",
	"FB_SUCODE"
);

ALTER TABLE "FREEBOARDCOMMENT" ADD CONSTRAINT "PK_FREEBOARDCOMMENT" PRIMARY KEY (
	"FC_CODE",
	"FC_FBCODE",
	"FC_FBSUCODE",
	"FC_FBDATE",
	"FC_SUCODE"
);

ALTER TABLE "FREEBOARDPHOTO" ADD CONSTRAINT "PK_FREEBOARDPHOTO" PRIMARY KEY (
	"FP_CODE",
	"FP_FBCODE",
	"FP_FBSUCODE",
	"FP_FBDATE"
);

ALTER TABLE "INFOBOARD" ADD CONSTRAINT "PK_INFOBOARD" PRIMARY KEY (
	"IB_CODE",
	"IB_DATE"
);

ALTER TABLE "SCHEDULE" ADD CONSTRAINT "PK_SCHEDULE" PRIMARY KEY (
	"SC_CODE",
	"SC_DATE",
	"SC_SUCODE"
);

ALTER TABLE "HEALTHDIARY" ADD CONSTRAINT "PK_HEALTHDIARY" PRIMARY KEY (
	"HD_CODE",
	"HD_DATE",
	"HD_CACODE",
	"HD_BBSUCODE",
	"HD_BBCODE"
);

ALTER TABLE "DOCTOR" ADD CONSTRAINT "PK_DOCTOR" PRIMARY KEY (
	"DO_CODE",
	"DO_COCODE"
);

ALTER TABLE "RESERVATIONCATEGORY" ADD CONSTRAINT "PK_RESERVATIONCATEGORY" PRIMARY KEY (
	"RC_CODE"
);

ALTER TABLE "FREEBOARDLIKE" ADD CONSTRAINT "PK_FREEBOARDLIKE" PRIMARY KEY (
	"FL_FBCODE",
	"FL_FBSUCODE",
	"FL_FBDATE",
	"FL_SUCODE"
);

ALTER TABLE "DAILYDIARYLIKE" ADD CONSTRAINT "PK_DAILYDIARYLIKE" PRIMARY KEY (
	"DL_DDCODE",
	"DL_DDSUCODE",
	"DL_DDDATE",
	"DL_SUCODE"
);

ALTER TABLE "INFOBOARDLIKE" ADD CONSTRAINT "PK_INFOBOARDLIKE" PRIMARY KEY (
	"IL_IBCODE",
	"IL_IBDATE",
	"IL_SUCODE"
);








-- CONSTRAINT : FK

ALTER TABLE "AUTHLOG" ADD CONSTRAINT "FK_SOCIALUSER_TO_AUTHLOG_1" FOREIGN KEY (
	"AL_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "COMPANYLOG" ADD CONSTRAINT "FK_COMPANY_TO_COMPANYLOG_1" FOREIGN KEY (
	"CL_COCODE"
)
REFERENCES "COMPANY" (
	"CO_CODE"
);

ALTER TABLE "BABY" ADD CONSTRAINT "FK_SOCIALUSER_TO_BABY_1" FOREIGN KEY (
	"BB_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "MAPCOMMENT" ADD CONSTRAINT "FK_COMPANY_TO_MAPCOMMENT_1" FOREIGN KEY (
	"MC_COCODE"
)
REFERENCES "COMPANY" (
	"CO_CODE"
);

ALTER TABLE "MAPCOMMENT" ADD CONSTRAINT "FK_SOCIALUSER_TO_MAPCOMMENT_1" FOREIGN KEY (
	"MC_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "RESERVATION" ADD CONSTRAINT "FK_DOCTOR_TO_RESERVATION_1" FOREIGN KEY (
	"RE_DOCOCODE"
)
REFERENCES "DOCTOR" (
	"DO_COCODE"
);

ALTER TABLE "RESERVATION" ADD CONSTRAINT "FK_DOCTOR_TO_RESERVATION_2" FOREIGN KEY (
	"RE_DOCODE"
)
REFERENCES "DOCTOR" (
	"DO_CODE"
);

ALTER TABLE "RESERVATION" ADD CONSTRAINT "FK_RESERVATIONCATEGORY_TO_RESERVATION_1" FOREIGN KEY (
	"RE_RCCODE"
)
REFERENCES "RESERVATIONCATEGORY" (
	"RC_CODE"
);

ALTER TABLE "RESERVATION" ADD CONSTRAINT "FK_BABY_TO_RESERVATION_1" FOREIGN KEY (
	"RE_BBSUCODE"
)
REFERENCES "BABY" (
	"BB_SUCODE"
);

ALTER TABLE "RESERVATION" ADD CONSTRAINT "FK_BABY_TO_RESERVATION_2" FOREIGN KEY (
	"RE_BBCODE"
)
REFERENCES "BABY" (
	"BB_CODE"
);

ALTER TABLE "HASHTAG" ADD CONSTRAINT "FK_DAILYDIARY_TO_HASHTAG_1" FOREIGN KEY (
	"HT_DDCODE"
)
REFERENCES "DAILYDIARY" (
	"DD_CODE"
);

ALTER TABLE "HASHTAG" ADD CONSTRAINT "FK_DAILYDIARY_TO_HASHTAG_2" FOREIGN KEY (
	"HT_DDSUCODE"
)
REFERENCES "DAILYDIARY" (
	"DD_SUCODE"
);

ALTER TABLE "HASHTAG" ADD CONSTRAINT "FK_DAILYDIARY_TO_HASHTAG_3" FOREIGN KEY (
	"HT_DDDATE"
)
REFERENCES "DAILYDIARY" (
	"DD_DATE"
);

ALTER TABLE "DAILYDIARY" ADD CONSTRAINT "FK_SOCIALUSER_TO_DAILYDIARY_1" FOREIGN KEY (
	"DD_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "DAILYDIARYPHOTO" ADD CONSTRAINT "FK_DAILYDIARY_TO_DAILYDIARYPHOTO_1" FOREIGN KEY (
	"DP_DDCODE"
)
REFERENCES "DAILYDIARY" (
	"DD_CODE"
);

ALTER TABLE "DAILYDIARYPHOTO" ADD CONSTRAINT "FK_DAILYDIARY_TO_DAILYDIARYPHOTO_2" FOREIGN KEY (
	"DP_DDSUCODE"
)
REFERENCES "DAILYDIARY" (
	"DD_SUCODE"
);

ALTER TABLE "DAILYDIARYPHOTO" ADD CONSTRAINT "FK_DAILYDIARY_TO_DAILYDIARYPHOTO_3" FOREIGN KEY (
	"DP_DDDATE"
)
REFERENCES "DAILYDIARY" (
	"DD_DATE"
);

ALTER TABLE "DAILYDIARYCOMMENT" ADD CONSTRAINT "FK_DAILYDIARY_TO_DAILYDIARYCOMMENT_1" FOREIGN KEY (
	"DC_DDCODE"
)
REFERENCES "DAILYDIARY" (
	"DD_CODE"
);

ALTER TABLE "DAILYDIARYCOMMENT" ADD CONSTRAINT "FK_DAILYDIARY_TO_DAILYDIARYCOMMENT_2" FOREIGN KEY (
	"DC_DDSUCODE"
)
REFERENCES "DAILYDIARY" (
	"DD_SUCODE"
);

ALTER TABLE "DAILYDIARYCOMMENT" ADD CONSTRAINT "FK_DAILYDIARY_TO_DAILYDIARYCOMMENT_3" FOREIGN KEY (
	"DC_DDDATE"
)
REFERENCES "DAILYDIARY" (
	"DD_DATE"
);

ALTER TABLE "DAILYDIARYCOMMENT" ADD CONSTRAINT "FK_SOCIALUSER_TO_DAILYDIARYCOMMENT_1" FOREIGN KEY (
	"DC_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "FREEBOARD" ADD CONSTRAINT "FK_SOCIALUSER_TO_FREEBOARD_1" FOREIGN KEY (
	"FB_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "FREEBOARDCOMMENT" ADD CONSTRAINT "FK_FREEBOARD_TO_FREEBOARDCOMMENT_1" FOREIGN KEY (
	"FC_FBCODE"
)
REFERENCES "FREEBOARD" (
	"FB_CODE"
);

ALTER TABLE "FREEBOARDCOMMENT" ADD CONSTRAINT "FK_FREEBOARD_TO_FREEBOARDCOMMENT_2" FOREIGN KEY (
	"FC_FBSUCODE"
)
REFERENCES "FREEBOARD" (
	"FB_SUCODE"
);

ALTER TABLE "FREEBOARDCOMMENT" ADD CONSTRAINT "FK_FREEBOARD_TO_FREEBOARDCOMMENT_3" FOREIGN KEY (
	"FC_FBDATE"
)
REFERENCES "FREEBOARD" (
	"FB_DATE"
);

ALTER TABLE "FREEBOARDCOMMENT" ADD CONSTRAINT "FK_SOCIALUSER_TO_FREEBOARDCOMMENT_1" FOREIGN KEY (
	"FC_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "FREEBOARDPHOTO" ADD CONSTRAINT "FK_FREEBOARD_TO_FREEBOARDPHOTO_1" FOREIGN KEY (
	"FP_FBCODE"
)
REFERENCES "FREEBOARD" (
	"FB_CODE"
);

ALTER TABLE "FREEBOARDPHOTO" ADD CONSTRAINT "FK_FREEBOARD_TO_FREEBOARDPHOTO_2" FOREIGN KEY (
	"FP_FBSUCODE"
)
REFERENCES "FREEBOARD" (
	"FB_SUCODE"
);

ALTER TABLE "FREEBOARDPHOTO" ADD CONSTRAINT "FK_FREEBOARD_TO_FREEBOARDPHOTO_3" FOREIGN KEY (
	"FP_FBDATE"
)
REFERENCES "FREEBOARD" (
	"FB_DATE"
);

ALTER TABLE "SCHEDULE" ADD CONSTRAINT "FK_SOCIALUSER_TO_SCHEDULE_1" FOREIGN KEY (
	"SC_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "HEALTHDIARY" ADD CONSTRAINT "FK_CATEGORY_TO_HEALTHDIARY_1" FOREIGN KEY (
	"HD_CACODE"
)
REFERENCES "CATEGORY" (
	"CA_CODE"
);

ALTER TABLE "HEALTHDIARY" ADD CONSTRAINT "FK_BABY_TO_HEALTHDIARY_1" FOREIGN KEY (
	"HD_BBSUCODE"
)
REFERENCES "BABY" (
	"BB_SUCODE"
);

ALTER TABLE "HEALTHDIARY" ADD CONSTRAINT "FK_BABY_TO_HEALTHDIARY_2" FOREIGN KEY (
	"HD_BBCODE"
)
REFERENCES "BABY" (
	"BB_CODE"
);

ALTER TABLE "DOCTOR" ADD CONSTRAINT "FK_COMPANY_TO_DOCTOR_1" FOREIGN KEY (
	"DO_COCODE"
)
REFERENCES "COMPANY" (
	"CO_CODE"
);

ALTER TABLE "FREEBOARDLIKE" ADD CONSTRAINT "FK_FREEBOARD_TO_FREEBOARDLIKE_1" FOREIGN KEY (
	"FL_FBCODE"
)
REFERENCES "FREEBOARD" (
	"FB_CODE"
);

ALTER TABLE "FREEBOARDLIKE" ADD CONSTRAINT "FK_FREEBOARD_TO_FREEBOARDLIKE_2" FOREIGN KEY (
	"FL_FBSUCODE"
)
REFERENCES "FREEBOARD" (
	"FB_SUCODE"
);

ALTER TABLE "FREEBOARDLIKE" ADD CONSTRAINT "FK_FREEBOARD_TO_FREEBOARDLIKE_3" FOREIGN KEY (
	"FL_FBDATE"
)
REFERENCES "FREEBOARD" (
	"FB_DATE"
);

ALTER TABLE "FREEBOARDLIKE" ADD CONSTRAINT "FK_SOCIALUSER_TO_FREEBOARDLIKE_1" FOREIGN KEY (
	"FL_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "DAILYDIARYLIKE" ADD CONSTRAINT "FK_DAILYDIARY_TO_DAILYDIARYLIKE_1" FOREIGN KEY (
	"DL_DDCODE"
)
REFERENCES "DAILYDIARY" (
	"DD_CODE"
);

ALTER TABLE "DAILYDIARYLIKE" ADD CONSTRAINT "FK_DAILYDIARY_TO_DAILYDIARYLIKE_2" FOREIGN KEY (
	"DL_DDSUCODE"
)
REFERENCES "DAILYDIARY" (
	"DD_SUCODE"
);

ALTER TABLE "DAILYDIARYLIKE" ADD CONSTRAINT "FK_DAILYDIARY_TO_DAILYDIARYLIKE_3" FOREIGN KEY (
	"DL_DDDATE"
)
REFERENCES "DAILYDIARY" (
	"DD_DATE"
);

ALTER TABLE "DAILYDIARYLIKE" ADD CONSTRAINT "FK_SOCIALUSER_TO_DAILYDIARYLIKE_1" FOREIGN KEY (
	"DL_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);

ALTER TABLE "INFOBOARDLIKE" ADD CONSTRAINT "FK_INFOBOARD_TO_INFOBOARDLIKE_1" FOREIGN KEY (
	"IL_IBCODE"
)
REFERENCES "INFOBOARD" (
	"IB_CODE"
);

ALTER TABLE "INFOBOARDLIKE" ADD CONSTRAINT "FK_INFOBOARD_TO_INFOBOARDLIKE_2" FOREIGN KEY (
	"IL_IBDATE"
)
REFERENCES "INFOBOARD" (
	"IB_DATE"
);

ALTER TABLE "INFOBOARDLIKE" ADD CONSTRAINT "FK_SOCIALUSER_TO_INFOBOARDLIKE_1" FOREIGN KEY (
	"IL_SUCODE"
)
REFERENCES "SOCIALUSER" (
	"SU_CODE"
);






-- SYNONYM

CREATE SYNONYM SU FOR SOCIALUSER;

CREATE SYNONYM CA FOR CATEGORY;

CREATE SYNONYM CO FOR COMPANY;

CREATE SYNONYM AL FOR AUTHLOG;

CREATE SYNONYM CL FOR COMPANYLOG;

CREATE SYNONYM BB FOR BABY;

CREATE SYNONYM MC FOR MAPCOMMENT;

CREATE SYNONYM RE FOR RESERVATION;

CREATE SYNONYM HT FOR HASHTAG;

CREATE SYNONYM DD FOR DAILYDIARY;

CREATE SYNONYM DP FOR DAILYDIARYPHOTO;

CREATE SYNONYM DC FOR DAILYDIARYCOMMENT;

CREATE SYNONYM FB FOR FREEBOARD;

CREATE SYNONYM FC FOR FREEBOARDCOMMENT;

CREATE SYNONYM FP FOR FREEBOARDPHOTO;

CREATE SYNONYM IB FOR INFOBOARD;

CREATE SYNONYM SC FOR SCHEDULE;

CREATE SYNONYM HD FOR HEALTHDIARY;

CREATE SYNONYM DO FOR DOCTOR;

CREATE SYNONYM RC FOR RESERVATIONCATEGORY;

CREATE SYNONYM FL FOR FREEBOARDLIKE;

CREATE SYNONYM DL FOR DAILYDIARYLIKE;

CREATE SYNONYM IL FOR INFOBOARDLIKE;

-- VIEW

-- 건강 추세에 따른 테이블
CREATE OR REPLACE VIEW HEALTHSTATUS AS
SELECT  HD.HDCODE,
        HD.SUCODE,
        HD.HDDATE,
        HD.AGE, -- 작성당시 아이 나이
        BB.TOAGE, -- 현재 아이 나이
        BB.BBCODE,
        BB.BBNAME,
        WEIGHT.WEIGHT AS BBWEIGHT,
        HEIGHT.HEIGHT AS BBHEIGHT,
        HEAD.HEAD AS HEAD
FROM (SELECT  HD_CODE AS HDCODE,
              HD_BBSUCODE AS SUCODE,
              HD_BBCODE AS BBCODE,
              HD_DATE AS HDDATE,
              TRUNC(MONTHS_BETWEEN(HD_DATE, TO_DATE(BB_BIRTHDAY))/12) AS AGE
      FROM HD INNER JOIN BB ON HD_BBCODE = BB_CODE AND HD_BBSUCODE = BB_SUCODE) HD 
      INNER JOIN (SELECT BB_SUCODE AS SUCODE,
                         BB_CODE AS BBCODE,
                         BB_NAME AS BBNAME,
                         TRUNC(MONTHS_BETWEEN(SYSDATE,TO_DATE(BB_BIRTHDAY))/12) AS TOAGE 
                  FROM BB) BB ON HD.SUCODE = BB.SUCODE AND HD.BBCODE = BB.BBCODE
      LEFT OUTER JOIN (SELECT   CA_CODE AS CACODE,
                                CA_NAME AS CANAME,
                                HD_CODE AS HDCODE,
                                HD_BBSUCODE AS SUCODE,
                                HD_VALUE AS WEIGHT
                       FROM CA INNER JOIN HD ON CA_CODE = HD_CACODE
                       WHERE CA_CODE = '01') WEIGHT ON WEIGHT.HDCODE = HD.HDCODE AND WEIGHT.SUCODE = HD.SUCODE
      LEFT OUTER JOIN (SELECT   CA_CODE AS CACODE,
                                CA_NAME AS CANAME,
                                HD_CODE AS HDCODE,
                                HD_BBSUCODE AS SUCODE,
                                HD_VALUE AS HEIGHT
                       FROM CA INNER JOIN HD ON CA_CODE = HD_CACODE
                       WHERE CA_CODE = '02') HEIGHT ON HEIGHT.HDCODE = HD.HDCODE AND HEIGHT.SUCODE = HD.SUCODE
      LEFT OUTER JOIN (SELECT   CA_CODE AS CACODE,
                                CA_NAME AS CANAME,
                                HD_CODE AS HDCODE,
                                HD_BBSUCODE AS SUCODE,
                                HD_VALUE AS HEAD
                       FROM CA INNER JOIN HD ON CA_CODE = HD_CACODE
                       WHERE CA_CODE = '03') HEAD ON HEAD.HDCODE = HD.HDCODE AND HEAD.SUCODE = HD.SUCODE
GROUP BY HD.HDCODE, HD.SUCODE, HD.HDDATE, HD.AGE, BB.BBCODE, BB.BBNAME, BB.TOAGE, WEIGHT.WEIGHT, HEIGHT.HEIGHT, HEAD.HEAD;
