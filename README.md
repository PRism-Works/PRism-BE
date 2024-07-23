# We are PRism's Backend Dev Team.

## Introduce PJT's Architecture

### 1. AWS infra Architecture
![image](https://github.com/user-attachments/assets/38677a94-9be8-4ab0-a926-ed7bbe614af2)

### 2. Our Infra Architecture
We developed using a serverless architecture. We registered issues on GitHub, treating them as tickets. Once development was complete, we merged the respective ticket into the main branch. We then pulled the latest code from the main branch to the local environment and deployed it through SAM CI/CD.

Based on the .yml file, we configured the infrastructure on AWS CloudFormation, which includes AWS API Gateway, AWS Lambda, AWS S3, and more. Logs from the Spring Boot server running inside AWS Lambda were recorded in Amazon CloudWatch.

### 3. AWS 인프라 아키텍처 구성 이유
AWS 인프라 아키텍처를 이렇게 구성한 이유를 설명합니다. 예를 들어, 특정 서비스나 구성 방식을 선택한 이유에 대해 설명합니다.

## DB 구조 소개

### 1. DB ERD 이미지
![DB ERD](path_to_your_image.png)
<!-- 위에 실제 이미지 경로를 넣어주세요 -->

### 2. DB 설계 설명
DB 설계를 어떻게 했는지 여기에 작성하세요. 예를 들어, 각 테이블의 역할과 주요 관계에 대해 설명합니다.

## 사용한 기술들 소개

### 1. 사용한 기술 목록
- 기술 1
- 기술 2
- 기술 3
<!-- 여기에 실제 사용한 기술을 나열하세요 -->

### 2. 기술 스택 선정 이유
기술 스택을 선정한 이유를 여기에 작성하세요. 예를 들어, 특정 기술을 선택한 이유와 그 기술이 프로젝트에 어떻게 기여했는지 설명합니다.
