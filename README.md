***
# 와인CRUD구현
:ab: 사용언어 : kotlin <br>
:computer: 개발환경: SpringBoot<br>


## 순서
① 엔티티 설계<br>
② CRUD서비스 구현<br>
③ 컨트롤러<br>
***
## 목표 

* [data](data) 폴더의 파일을 참고하여 entity 설계
* 해당 데이터들을 삽입, 수정, 삭제, 조회할 수 있는 API 구현
* 조회 기능은 다음을 포함
    * 와인 조회
        * 필터링: 와인 종류, 알코올 도수 범위, 와인의 가격 범위, 와인의 스타일, 와인의 등급, 지역
        * 정렬: 와인 이름, 알코올 도수, 산도, 바디감, 단맛, 타닌, 와인의 점수, 와인의 가격
        * 검색: 와인 이름
    * 와이너리 조회
        * 필터링: 지역
        * 정렬: 와이너리 이름
        * 검색: 와이너리 이름
    * 포도 품종 조회
        * 필터링: 지역
        * 정렬: 포도 품종 이름, 산도, 바디감, 단맛, 타닌
        * 검색: 포도 품종 이름
    * 지역 조회
        * 필터링: 상위 지역
        * 정렬: 지역 이름
        * 검색: 지역 이름
    * 수입사 조회
        * 정렬: 수입사 이름
        * 검색: 수입사 이름
* 조회 기능은 다음 정보를 제공
    * 와인 조회
        * 다수 조회 시: 와인의 종류, 와인 이름, 최상위 지역 이름
        * 단일 조회 시: 와인의 종류, 와인 이름, 알코올 도수, 산도, 바디감, 단맛, 타닌, 와인의 점수, 와인의 가격, 와인의 스타일, 와인의 등급, 수입사 이름, 와이너리 이름, 와이너리
          지역, 지역 이름 및 모든 상위 지역 이름, 와인의 향, 와인과 어울리는 음식, 포도 품종
    * 와이너리 조회
        * 다수 조회 시: 와이너리 이름, 지역 이름
        * 단일 조회 시: 와이너리 이름, 지역 이름, 와이너리의 와인
    * 포도 품종 조회
        * 다수 조회 시: 포도 품종 이름, 지역 이름
        * 단일 조회 시: 포도 품종 이름, 산도, 바디감, 단맛, 타닌, 지역 이름, 포도 품종의 와인
    * 지역 조회
        * 다수 조회 시: 지역 이름
        * 단일 조회 시: 지역 이름, 모든 상위 지역 이름, 포도 품종, 와이너리 이름, 와인 이름
    * 수입사 조회
        * 다수 조회 시: 수입사 이름
        * 단일 조회 시: 수입사 이름, 수입사의 와인

***
## Data 설명

### Wine

#### wine.csv

* `type`: 와인의 종류
* `name_korean`: 와인 이름(한글)
* `name_english`: 와인 이름(영어)
* `alcohol`: 알코올 도수
* `acidity`: 산도
* `body`: 바디감
* `sweetness`: 단맛
* `tannin`: 타닌
* `serving_temperature`: 권장섭취온도
* `score`: 와인의 점수
* `price`: 와인의 가격
* `style`: 와인의 스타일
* `grade`: 와인의 등급
* `importer`: 수입사
* `winery_name_korean`: 와이너리 이름(한글)
* `winery_name_english`: 와이너리 이름(영어)
* `region_name_korean`: 지역 이름(한글)
* `region_name_english`: 지역 이름(영어)

#### wine_aroma.csv

* `name_korean`: 와인 이름(한글)
* `name_english`: 와인 이름(영어)
* `aroma`: 와인의 향

#### wine_pairing.csv

* `name_korean`: 와인 이름(한글)
* `name_english`: 와인 이름(영어)
* `pairing`: 와인과 어울리는 음식

#### wine_grape.csv

* `name_korean`: 와인 이름(한글)
* `name_english`: 와인 이름(영어)
* `grape_name_korean`: 포도 품종(한글)
* `grape_name_english`: 포도 품종(영어)

### Winery

#### winery.csv

* `name_korean`: 와이너리 이름(한글)
* `name_english`: 와이너리 이름(영어)
* `region_name_korean`: 지역 이름(한글)
* `region_name_english`: 지역 이름(영어)

### Grape

#### grape.csv

* `name_korean`: 포도 품종(한글)
* `name_english`: 포도 품종(영어)
* `acidity`: 산도
* `body`: 바디감
* `sweetness`: 단맛
* `tannin`: 타닌

#### grape_share.csv

* `name_korean`: 포도 품종(한글)
* `name_english`: 포도 품종(영어)
* `share`: 포도 품종의 비율
* `region_name_korean`: 지역 이름(한글)
* `region_name_english`: 지역 이름(영어)

### Region

#### region.csv

* `name_korean`: 지역 이름(한글)
* `name_english`: 지역 이름(영어)
* `parent_name_korean`: 상위 지역 이름(한글)
* `parent_name_english`: 상위 지역 이름(영어)
***

## 구현내용
1.엔티티 생성
-Wine
-WineAroma
-WineParing
-Winery
-Region
-Grape
-GrapeShare
-Importer

2.각 엔티티별 레포지토리 생성
*Querydsl사용

3.CRUD서비스
 -WineService
-WineryService
-RegionService
-GrapeService
-ImporterService
*페이징 처리

4.각 서비스 별 컨트롤러 구현
* JSON사용

나머지 데이터들은
-GrapeShare
-WineAroma
-WineParing
관련 엔티티     @OneToOne 매핑  즉시로딩, caseCade 기능을 이용해서 CRUD가 쉽게 되도록 구현

