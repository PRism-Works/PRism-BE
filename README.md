# We are PRism's Backend Dev Team. (우리는 PRism의 백엔드 개발 팀입니다.)

## Introduce PJT's Architecture (PJT의 아키텍처를 소개합니다.)

### 1. AWS infra Architecture (AWS 인프라 아키텍처)
![image](https://github.com/user-attachments/assets/38677a94-9be8-4ab0-a926-ed7bbe614af2)

### 2. Our Infra Architecture (우리의 인프라 아키텍처)
We developed using a serverless architecture. We registered issues on GitHub, treating them as tickets. Once development was complete, we merged the respective ticket into the main branch. We then pulled the latest code from the main branch to the local environment and deployed it through SAM CI/CD.

우리는 서버리스 아키텍처를 사용해서 개발했습니다. GitHub에 이슈를 등록하고 티켓과 매핑된 branch를 매번 만들어서 Issue를 처리했습니다. 개발이 완료되면 해당 티켓을 메인 브랜치에 병합했습니다. 그 후, 메인 브랜치에서 최신 코드를 로컬 환경으로 가져와 SAM CI/CD를 통해 배포했습니다.

Based on the .yml file, we configured the infrastructure on AWS CloudFormation, which includes AWS API Gateway, AWS Lambda, AWS S3, and more. Logs from the Spring Boot server running inside AWS Lambda were recorded in Amazon CloudWatch.

.yml 파일을 기반으로 AWS CloudFormation에서 인프라를 구성했습니다. 여기엔 AWS API Gateway, AWS Lambda, AWS S3 등이 포함됩니다. AWS Lambda 내에서 실행되는 Spring Boot 서버의 로그는 Amazon CloudWatch에 기록되었습니다.

### 3. The reason for configuring the AWS infrastructure architecture this way. (AWS 인프라 아키텍처를 이렇게 구성한 이유)
![image](https://github.com/user-attachments/assets/f4aeca78-84bf-42dc-a6ab-a0681d0c8500)

Firstly, we determined that the PRism service would not experience high traffic immediately after entering the initial market. AWS Lambda offers 1 million free requests per month, and we could use AWS API Gateway in front of AWS Lambda. With AWS API Gateway, we designed an architecture that allows us to easily renew SSL certificates using our existing Gabia domain and AWS ACM.

첫째, PRism 서비스가 초기 시장에 진입한 직후에는 높은 트래픽을 경험하지 않을 것이라고 판단했습니다. AWS Lambda는 매월 100만 개의 무료 요청을 제공하며, AWS Lambda 앞에 AWS API Gateway를 사용할 수 있습니다. AWS API Gateway를 사용함으로써, 기존의 Gabia 도메인과 AWS ACM을 사용하여 SSL 인증서를 쉽게 갱신할 수 있는 아키텍처를 설계했습니다.

AWS Lambda conveniently had a snapshot feature, and we determined that the cold start issues with Spring Boot in AWS Lambda were somewhat resolved. Thus, our Spring Boot server was deployed within AWS Lambda. We believed that even if multiple Spring Boot servers were created, they could be deployed to multiple Lambdas, making development and management fast and convenient.

AWS Lambda에 스냅샷 기능이 제공되기 시작할 때부터 눈여겨 보고 있었습니다. LG CNS의 기술 블로그를 보고 관심을 가지게 되었어요. AWS Lambda에는 Cold Start문제가 있었는데, 그래서 보통 Python으로 개발을 했었어요. SnapShot 기능이 추가된 이후로 AWS Lambda에서 Spring Boot의 콜드 스타트 문제가 어느 정도 해결되었다고 판단했습니다. 그래서 우리의 Spring Boot 서버를 AWS Lambda 내에 배포했습니다. 우리는 여러 Spring Boot 서버가 생성되더라도 이들을 여러 Lambda에 배포할 수 있어 개발과 관리가 빠르고 편리하다고 판단했고, 이외에도 여러 장점이 있었어요.

During the month and a half of development, there were no costs associated with using the AWS infrastructure, resulting in significant cost savings compared to using EC2 for development. Although there were basic costs for ACM renewal and API Gateway, these were much lower than the costs of keeping EC2 instances running continuously.

개발 기간 동안 한 달 반 동안 AWS 인프라를 사용하는 데 드는 비용이 전혀 없었습니다. EC2를 사용하여 개발하는 것에 비해 상당한 비용 절감 효과가 있었습니다. ACM 갱신과 API Gateway에 대한 기본 비용은 있었지만, EC2 인스턴스를 지속적으로 운영하는 비용보다 훨씬 낮았습니다.

### 4. DB ERD Image (DB ERD 이미지)
![img](https://github.com/user-attachments/assets/83ce6049-f74e-4108-801f-e423465adfc7)

### 2. Explanation of the Database Design(2. 데이터베이스 디자인 설명)
When we started development, the database was simple. As development progressed, there were many factors we needed to consider. We had to figure out how to handle anonymous users and JWT login tokens. Initially, when creating entities using JPA, we designed some tables with @CollectionTable, only to later discover that they could not use querydsl. Because of this, we had to redesign all the tables as entities and redevelop them.

개발을 시작했을 때 데이터베이스는 간단했습니다. 개발이 진행됨에 따라 고려해야 할 여러 요소들이 생겼습니다. 익명 사용자와 JWT 로그인 토큰을 어떻게 처리할지 파악해야 했습니다. 처음에 JPA를 사용하여 엔티티를 생성할 때 @CollectionTable로 몇몇 테이블을 설계했으나, 나중에 이들이 Querydsl을 사용할 수 없다는 사실을 발견했습니다. 이로 인해 모든 테이블을 엔티티로 다시 설계하고 개발을 다시 진행해야 했습니다.

## Technologies Used AND Reasons for Choosing the Technology Stack (사용된 기술 및 기술 스택 선택 이유)
We wanted our service to be sustainable over a long period of time and took a practical approach. To maintain the service for an extended duration, we had to implement a low-cost, high-performance server using an efficient architecture.

우리는 서비스가 장기간 지속될 수 있도록 매우 현실적으로 아키텍처에 대한 고민을 했어요. 서비스를 오랫동안 유지하기 위해, 효율적인 아키텍처를 사용해서 저비용 고성능 서버를 구현해야 했습니다.

We used Oracle Cloud's free instance for our database, implementing a permanently free DB with 1GB of RAM and 50GB of storage. Additionally, we utilized AWS infrastructure to implement a Serverless architecture and purchased a domain from Gabia at approximately $7 per year. AWS Lambda supports up to 1 million free invocations per month, and AWS ApiGateway only requires a basic payment of about $3. Logging is managed via CloudWatch, and we set up a Serverless CI/CD environment using the SAM CLI. A significant convenience was that, unlike using Jenkins, there was no need to set up a separate server, and unlike AWS CodeSeries, complex .yml files were not required. Moreover, unlike traditional CI/CD, deployments could occur rapidly from any branch without targeting specific events, which was possible because our two backend developers could deploy from any branch at any time. Deploying a Spring Boot Server on AWS Lambda resolved issues like manual autoscaling in CD that were present when using Jenkins, as AWS Lambda automatically handles autoscaling. Issues of server crashes and the lack of automatic rollback when using Jenkins were also resolved with AWS CloudFormation's stack rollback. Although AWS CodeSeries also supports rollback, the ease of using SAM CLI, which combines the advantages of both, continuously impressed us.

우리는 데이터베이스를 Oracle Cloud의 무료 인스턴스를 사용해서 1GB의 RAM과 50GB의 저장 공간을 갖춘 영구적으로 무료인 DB를 구현했습니다. 또한, AWS 인프라를 활용하여 서버리스 아키텍처를 구현하고, Gabia에서 연간 약 7달러에 도메인을 구매했습니다. AWS Lambda는 매월 최대 100만 번의 무료 호출을 지원하고, AWS ApiGateway는 기본적으로 약 3달러의 비용만 지불하면 됩니다. 로깅은 CloudWatch를 통해 관리되며, SAM CLI를 사용하여 서버리스 CI/CD 환경을 구축했습니다. Jenkins를 사용할 때와는 달리 별도의 서버를 설치할 필요가 없고, AWS CodeSeries와 달리 복잡한 .yml 파일이 필요하지 않다는 점이 큰 편리함이었습니다. 전통적인 CI/CD와 달리, 특정 이벤트를 대상으로 하지 않고 어떤 브랜치에서도 빠르게 배포할 수 있었는데, 배포 히스토리를 CloudFormation에서 Stack형태로 관리하고, 배포 프로세스에서 발생하는 문제도 CloudWatch도 기록되고 혹시모를 사고에 Rollback기능도 지원이 되어서 무척 편하게 SAM CLI를 사용해 개발했습니다. AWS Lambda에 Spring Boot 서버를 배포함으로써 Jenkins를 사용할 때 발생했던 CD에서의 수동 오토스케일링 문제가 해결되었고, 서버가 다운되었을 때 자동으로 롤백되지 않는 문제도 AWS CloudFormation의 스택 롤백으로 해결되었습니다. AWS CodeSeries도 롤백을 지원하지만, SAM CLI의 사용 편의성이 두 기술의 장점을 결합한 것보다 좋다고 느꼈습니다.

Additionally, during the initial launch of our service, we increased the log level, resulting in a significant accumulation of logs that needed to be managed. By using CloudWatch, we no longer had to worry about running out of hard drive capacity for logging or the system resources required for storing logs in real time.

또한, 서비스 초기 출시 당시 로그 레벨을 높여 상당한 양의 로그가 쌓이게 되고, 이걸 관리해야 하는데, CloudWatch를 사용함으로써 로깅을 위한 하드 드라이브 용량이나 실시간 로그 저장을 위한 시스템 자원에 대한 걱정을 더 이상 하지 않아도 되었습니다.

결론적으로 개발에만 집중했고, 6주동안 편하게 개발했어요

### List of Technologies Used
- AWS Lambda
- AWS ApiGateWay
- AWS CloudFormation
- AWS CloudWatch
- SAM CLI
- Spring Boot
- ...
