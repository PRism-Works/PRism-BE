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
![DB ERD](path_to_your_image.png)
<!-- 위에 실제 이미지 경로를 넣어주세요 -->

### 2. Explanation of the Database Design
Describe how the database was designed here. For example, explain the role of each table and the key relationships.

## Technologies Used

### List of Technologies Used
- Technology 1
- Technology 2
- Technology 3

### Reasons for Choosing the Technology Stack
Describe the reasons for choosing the technology stack here. For example, explain why specific technologies were selected and how they contributed to the project.
