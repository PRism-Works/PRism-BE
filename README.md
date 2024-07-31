# We are PRism's Backend Dev Team.

## Introduce PJT's Architecture

### 1. AWS infra Architecture
![image](https://github.com/user-attachments/assets/38677a94-9be8-4ab0-a926-ed7bbe614af2)

### 2. Our Infra Architecture
We developed using a serverless architecture. We registered issues on GitHub, treating them as tickets. Once development was complete, we merged the respective ticket into the main branch. We then pulled the latest code from the main branch to the local environment and deployed it through SAM CI/CD.

Based on the .yml file, we configured the infrastructure on AWS CloudFormation, which includes AWS API Gateway, AWS Lambda, AWS S3, and more. Logs from the Spring Boot server running inside AWS Lambda were recorded in Amazon CloudWatch.

### 3. The reason for configuring the AWS infrastructure architecture this way.
![image](https://github.com/user-attachments/assets/f4aeca78-84bf-42dc-a6ab-a0681d0c8500)

Firstly, we determined that the PRism service would not experience high traffic immediately after entering the initial market. AWS Lambda offers 1 million free requests per month, and we could use AWS API Gateway in front of AWS Lambda. With AWS API Gateway, we designed an architecture that allows us to easily renew SSL certificates using our existing Gabia domain and AWS ACM.

AWS Lambda conveniently had a snapshot feature, and we determined that the cold start issues with Spring Boot in AWS Lambda were somewhat resolved. Thus, our Spring Boot server was deployed within AWS Lambda. We believed that even if multiple Spring Boot servers were created, they could be deployed to multiple Lambdas, making development and management fast and convenient.

During the month and a half of development, there were no costs associated with using the AWS infrastructure, resulting in significant cost savings compared to using EC2 for development. Although there were basic costs for ACM renewal and API Gateway, these were much lower than the costs of keeping EC2 instances running continuously.

### 1. DB ERD Image
![img](https://github.com/user-attachments/assets/83ce6049-f74e-4108-801f-e423465adfc7)

### 2. Explanation of the Database Design
When we started development, the database was simple. As development progressed, there were many factors we needed to consider. We had to figure out how to handle anonymous users and JWT login tokens. Initially, when creating entities using JPA, we designed some tables with @CollectionTable, only to later discover that they could not use querydsl. Because of this, we had to redesign all the tables as entities and redevelop them.

## Technologies Used AND Reasons for Choosing the Technology Stack
We wanted our service to be sustainable over a long period of time and took a practical approach. To maintain the service for an extended duration, we had to implement a low-cost, high-performance server using an efficient architecture.

We used Oracle Cloud's free instance for our database, implementing a permanently free DB with 1GB of RAM and 50GB of storage. Additionally, we utilized AWS infrastructure to implement a Serverless architecture and purchased a domain from Gabia at approximately $7 per year. AWS Lambda supports up to 1 million free invocations per month, and AWS ApiGateway only requires a basic payment of about $3. Logging is managed via CloudWatch, and we set up a Serverless CI/CD environment using the SAM CLI. A significant convenience was that, unlike using Jenkins, there was no need to set up a separate server, and unlike AWS CodeSeries, complex .yml files were not required. Moreover, unlike traditional CI/CD, deployments could occur rapidly from any branch without targeting specific events, which was possible because our two backend developers could deploy from any branch at any time. Deploying a Spring Boot Server on AWS Lambda resolved issues like manual autoscaling in CD that were present when using Jenkins, as AWS Lambda automatically handles autoscaling. Issues of server crashes and the lack of automatic rollback when using Jenkins were also resolved with AWS CloudFormation's stack rollback. Although AWS CodeSeries also supports rollback, the ease of using SAM CLI, which combines the advantages of both, continuously impressed us.

Additionally, during the initial launch of our service, we increased the log level, resulting in a significant accumulation of logs that needed to be managed. By using CloudWatch, we no longer had to worry about running out of hard drive capacity for logging or the system resources required for storing logs in real time.

### List of Technologies Used
- AWS Lambda
- AWS ApiGateWay
- AWS CloudFormation
- AWS CloudWatch
- SAM CLI
- Spring Boot
- ...
