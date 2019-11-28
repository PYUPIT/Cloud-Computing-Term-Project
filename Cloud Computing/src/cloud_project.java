import java.util.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
/* credential 설정 확인에 필요한 클래스들 호출*/
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
/* ListInstance() 메소드에 필요한 클래스들 호출 */
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Instance;
/* AvailabilityZones() 메소드에 필요한 클래스들 호출 */
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.AvailabilityZone;
/* StartInstance() 메소드에 필요한 클래스들 호출 */
import com.amazonaws.services.ec2.model.StartInstancesRequest;
/* AvailabilityRegions() 메소드에 필요한 클래스들 호출 */
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Region;
/* StopInstance() 메소드에 필요한 클래스들 호출 */
import com.amazonaws.services.ec2.model.StopInstancesRequest;
/* CreateInstance() 메소드에 필요한 클래스들 호출 */
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
/* RebootInstance() 메소드에 필요한 클래스들 호출 */
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
/* ListImages() 메소드에 필요한 클래스들 호출 */
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.Image;

public class cloud_project {
	
		static AmazonEC2 ec2;
		
		static Scanner input = new Scanner(System.in);
		
		private static void init() throws Exception {
			
			ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
			try {
				credentialsProvider.getCredentials();
			} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
					"Please make sure that your credentials file is at the correct " +
					"location (~/.aws/credentials), and is in valid format.", e);
			}
			
			ec2 = AmazonEC2ClientBuilder.standard()
					.withCredentials(credentialsProvider)
					.withRegion("us-east-1")
					.build();
		}
		
		public static void main(String[] args) throws Exception {
			
			init();
			
			boolean  loop_sign = true;
			
			while(loop_sign)
			{
				System.out.println(" ");
				System.out.println(" ");
				System.out.println("------------------------------------------------------------");
				System.out.println(" Cloud Computing Term Project : AWS CONTROL PANEL USING SDK ");
				System.out.println(" ");
				System.out.println(" Lecture : Cloud Computing | Department : Software");
				System.out.println(" ");
				System.out.println(" Made By Junho Jin At CBNU ");
				System.out.println("------------------------------------------------------------");
				System.out.println("  1. List instance			2. Available zones ");
				System.out.println("  3. Start instance			4. Available regions ");
				System.out.println("  5. Stop instance			6. Create instance ");
				System.out.println("  7. Reboot instance			8. List images ");
				System.out.println(" 99. Quit");
				System.out.println("------------------------------------------------------------");
				
				System.out.print("\n Please enter a number: ");
			
				int menu_num = input.nextInt();
				
				switch(menu_num)
				{
				case 1:
					ListInstances();
					break;
					
				case 2:
					AvailableZones();
					break;
					
				case 3:
					StartInstance();
					break;
					
				case 4:
					AvailableRegions();
					break;
					
				case 5:
					StopInstance();
					break;
					
				case 6:
					CreateInstance();
					break;
					
				case 7:
					RebootInstance();
					break;
					
				case 8:
					listImages();
					break;
					
				case 99:	// while문을 탈출한다.
					System.out.print("\n 프로그램을 종료합니다. \n");
					loop_sign = false;
					break;
					
				}	/* End Of switch() */
			}	/* End Of while() */
		}	/* End of main() */
		
		public static void ListInstances() {
		
			System.out.println("\n 인스턴스를 불러오는 중입니다 .... \n");
		
			DescribeInstancesRequest request = new DescribeInstancesRequest();
			
			boolean loop_sign = true;
			
			while(loop_sign)
			{	
				DescribeInstancesResult response = ec2.describeInstances(request);
				
				for(Reservation res : response.getReservations())
				{
					for(Instance instance : res.getInstances()) {
						
						System.out.printf(
								"[ID] %s \t | " + "[AMI] %s \t | " + "[TYPE] %s \t | " + "[STATE] %s \t | " + "[MONITORING STATE] %s \t | \n", 
								instance.getInstanceId(), 
								instance.getImageId(), 
								instance.getInstanceType(), 
								instance.getState().getName(), 
								instance.getMonitoring().getState());
						}
				}
				
				request.setNextToken(response.getNextToken());
				
				if(response.getNextToken() == null)
				{
					loop_sign = false;
				}
			}
		}
		
		public static void AvailableZones() {
			
			System.out.println("\n 가용영역을 불러오는 중입니다 .... \n");
	        
	        DescribeAvailabilityZonesResult zones_response = ec2.describeAvailabilityZones();
	        
	        for(AvailabilityZone zone : zones_response.getAvailabilityZones()) {
	        	System.out.printf("[ID] %s \t | " + "[REGION] %s \t | " + "[ZONE] %s \t | \n", 
	        			zone.getZoneId(), 
	        			zone.getRegionName(), 
	        			zone.getZoneName());
	        }
	        
	        System.out.printf("\n 당신은 %d개의  가용영역에 접근할 수 있습니다.\n", zones_response.getAvailabilityZones().size());
		}
		
		public static void StartInstance() {
			
			System.out.println("\n 인스턴트를 실행하는 중입니다 .... \n");
			
			System.out.print(" 실행할 인스턴스 ID를 입력해주세요  >> ");
			
			String instance_id = input.next();
			
			StartInstancesRequest start_Instances_Request = new StartInstancesRequest().withInstanceIds(instance_id);
			 
	        ec2.startInstances(start_Instances_Request);
		}
		
		public static void AvailableRegions() {
			
			System.out.println("가용리전을 불러오는 중입니다 ....");
			
	        DescribeRegionsResult regions_response = ec2.describeRegions();
	        
	        for(Region region : regions_response.getRegions())
	        {
	        	System.out.printf( "[REGION] %s \t | " + ", [ENDPOINT] %s \t | \n", region.getRegionName(), region.getEndpoint());
	        }
		}
		
		public static void StopInstance() {
			
			System.out.println(" 인스턴트를 종료하는 중입니다 ....");
			
			System.out.print("종료할 인스턴스의 ID를 입력해주세요 >> ");
			
			String instance_id = input.next();
			
			StopInstancesRequest stop_Instances_Request = new StopInstancesRequest().withInstanceIds(instance_id);

	        ec2.stopInstances(stop_Instances_Request).getStoppingInstances().get(0).getPreviousState().getName();
		}
		
		public static void CreateInstance() {
			
			System.out.println(" 인스턴스를 생성하는 중입니다 ....");
			
			System.out.print(" AMI ID를 입력해주세요 >> ");
			
			String ami_id = input.next();	// 인스턴트 생성에 필요한 AMI ID를 입력
			
			RunInstancesRequest run_Instances_Request = new RunInstancesRequest();
			
			run_Instances_Request.withImageId(ami_id)				// 인스턴트 생성에 필요한 AMI ID를 입력
							   .withInstanceType("t2.micro")	// 인스턴트 생성에 필요한 인스턴트 유형을 입력
			                   .withMinCount(1)
			                   .withMaxCount(1)
			                   .withKeyName("awskey01")			// 인스턴트 생성에 필요한 키페어를 입력
			                   .withSecurityGroups("rules_01");	// 인스턴트 생성에 필요한 보안그룹을 입력

			RunInstancesResult result = ec2.runInstances(run_Instances_Request);
			
			Instance instance = result.getReservation().getInstances().get(0);
			
			String instance_id = instance.getInstanceId();
			
			System.out.printf(" \" %s \" 인스턴스를 생성했습니다. \n", instance_id);
		}
		
		public static void RebootInstance() {
			
			System.out.println(" 인스턴스를 재부팅하는 중입니다 ....");
			
			System.out.print(" 재부팅할 인스턴스 ID를 입력해주세요 >> ");
			
			String instance_id = input.next();
			
			RebootInstancesRequest reboot_Instances_Request = new RebootInstancesRequest().withInstanceIds(instance_id);
			
			ec2.rebootInstances(reboot_Instances_Request);
		}
		
		public static void listImages() {
			
			System.out.println("\n 이미지를 불러오는 중입니다 .... \n");
			
			DescribeImagesRequest img_req = new DescribeImagesRequest();
			
			img_req.withOwners("self");
			
			DescribeImagesResult img_result = ec2.describeImages(img_req);
			
			List<Image> images = img_result.getImages();
			
			for (Image image : images)
			{
				System.out.printf("[ID] %s \t | [NAME] %s \t | [OWNER] %s \t | \n",
						image.getImageId(), 
						image.getName(), 
						image.getOwnerId());
			}
		}
}

