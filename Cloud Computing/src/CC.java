import java.util.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;

import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;

import com.amazonaws.services.ec2.model.InstanceNetworkInterfaceSpecification;

import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.Reservation;

import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.AvailabilityZone;

import com.amazonaws.services.ec2.model.StartInstancesRequest;	// start instance에 필요한 라이브러리

import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Region;

import com.amazonaws.services.ec2.model.StopInstancesRequest;

import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;

public class CC {
	
		static AmazonEC2 ec2;
		
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
		
		Scanner menu = new Scanner(System.in);
		Scanner id_string = new Scanner(System.in);
		boolean  loop_tag = true;
		int MenuNumber = 0;
		
		while(loop_tag)
		{
			System.out.println(" ");
			System.out.println(" ");
			System.out.println("------------------------------------------------------------");
			System.out.println(" Amazon AWS Control Panel using SDK ");
			System.out.println(" ");
			System.out.println(" Cloud Computing, Computer Science Department ");
			System.out.println(" at Chungbuk National University Made By Jin Jun-ho");
			System.out.println("------------------------------------------------------------");
			System.out.println(" 1. list instance		2. available zones ");
			System.out.println(" 3. start instance		4. available regions ");
			System.out.println(" 5. stop instance		6. create instance ");
			System.out.println(" 7. reboot instance		8. list images ");
			System.out.println(" 99. quit");
			System.out.println("------------------------------------------------------------");
			System.out.print("Enter an integer: ");
			
			MenuNumber = menu.nextInt();
			
			switch(MenuNumber)
			{
			case 1:
				listInstances();
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
			case 99:
				System.out.print("Bye~.");
				loop_tag = false;
				break;
			}
		}
		}
		
		public static void listInstances() {
		
			System.out.println("Listing instances....");
		
			boolean done = false;
		
			DescribeInstancesRequest request = new DescribeInstancesRequest();
		
			
			while(!done)
			{	
				DescribeInstancesResult response = ec2.describeInstances(request);
				
				for(Reservation reservation : response.getReservations())
				{
					for(Instance instance : reservation.getInstances()) {
						System.out.printf(
								"[id] %s, " + "[AMI] %s, " + "[type] %s, " + "[state] %10s, " + "[monitoring state] %s", 
								instance.getInstanceId(), 
								instance.getImageId(), 
								instance.getInstanceType(), 
								instance.getState().getName(), 
								instance.getMonitoring().getState());
						}
					System.out.println();
				}
				request.setNextToken(response.getNextToken());
				if(response.getNextToken() == null)
				{
					done = true;
				}
			}
		}
		
		public static void AvailableZones() {
			
			System.out.println("Available zones....");
	        
	        DescribeAvailabilityZonesResult zones_response = ec2.describeAvailabilityZones();
	        
	        for(AvailabilityZone zone : zones_response.getAvailabilityZones()) {
	        	System.out.printf( "[id] %s, " + "[region] %10s " + "[zone] %10s", zone.getZoneId(), zone.getRegionName(), zone.getZoneName());
	        	System.out.println();
	        }
	        
	        System.out.printf("\nYou have access to %d Availbility Zones.\n", zones_response.getAvailabilityZones().size());
		}
		
		public static void StartInstance() {
			
			System.out.println("Starting instance....");
			
			System.out.print("Enter an instance ID: ");
			
			Scanner input = new Scanner(System.in);
			
			String instance_id = input.next();
			
			StartInstancesRequest startInstancesRequest = new StartInstancesRequest().withInstanceIds(instance_id);
			 
	        ec2.startInstances(startInstancesRequest);
		}
		
		public static void AvailableRegions() {
			
			System.out.println("Available Regions....");
	        // 계정에 사용할 수 있는 리전을 나열하려면 AmazonEC2Client의 describeRegions 메서드를 호출
	        DescribeRegionsResult regions_response = ec2.describeRegions();
	        
	        for(Region region : regions_response.getRegions()) {
	        	System.out.printf( "[region] %20s " + ", [endpoint] %s", region.getRegionName(), region.getEndpoint());
	        	System.out.println();
	        }
		}
		
		public static void StopInstance() {
			
			System.out.println("Stopping instance....");
			
			System.out.print("Enter an instance ID: ");
			
			Scanner input = new Scanner(System.in);
			
			String instance_id = input.next();
			
			StopInstancesRequest stopInstancesRequest = new StopInstancesRequest().withInstanceIds(instance_id);

	        ec2.stopInstances(stopInstancesRequest).getStoppingInstances().get(0).getPreviousState().getName();
		}
		
		public static void CreateInstance() {
			
			System.out.println("Create instance....");
			
			RunInstancesRequest runInstancesRequest = new RunInstancesRequest();
			
			runInstancesRequest.withImageId("ami-0b7554904b554fb53")
							   .withInstanceType("t2.micro")
			                   .withMinCount(1)
			                   .withMaxCount(1)
			                   .withKeyName("awskey01")
			                   .withSecurityGroups("rules_01");

			RunInstancesResult result = ec2.runInstances(runInstancesRequest);
			
			Instance instance = result.getReservation().getInstances().get(0);
			String instance_id = instance.getInstanceId();
			
			System.out.printf("Create instance : %s \n", instance_id);
		}
		
		public static void RebootInstance() {
			
			System.out.println("Stopping instance....");
			
			System.out.print("Enter an instance ID: ");
			
			Scanner input = new Scanner(System.in);
			
			String instance_id = input.next();
			
			RebootInstancesRequest rebootInstancesRequest = new RebootInstancesRequest().withInstanceIds(instance_id);
			ec2.rebootInstances(rebootInstancesRequest);
		}
		
		public static void listImages() {
			
			System.out.println("Listing images....");
			
			DescribeImagesRequest img_req = new DescribeImagesRequest();
			
			img_req.withOwners("self");
			
			DescribeImagesResult img_result = ec2.describeImages(img_req);
			
			List<Image> images = img_result.getImages();
			
			for (Image image : images) {
				System.out.printf("[id]%s [name]%s \n",image.getImageId(), image.getName());
			}
		}
}

