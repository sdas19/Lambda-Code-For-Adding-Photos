import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddPhotosFunction implements RequestHandler<RequestClass,InsertImageResponse> {

    @Override
    public InsertImageResponse handleRequest(RequestClass input, Context context) {

        DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder
                                .EndpointConfiguration("dynamodb.ap-south-1.amazonaws.com",
                                "ap-south-1"
                        )
                ).build()
        );
        Table table = dynamoDB.getTable("SparkTestTable");
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Image>>() {}.getType();
            Item initialResult = table.getItem("userId", input.getUserId());
            List<Image> imageList = new ArrayList<>();
            if(initialResult!=null){
                List<Image> initialImages = gson.fromJson(initialResult.getString("imageUrls"),listType);
                if(initialImages!=null && !initialImages.isEmpty()){
                    imageList = initialImages;
                }
            }
            imageList.add(0,new Image(input.getImageUrl()));
            String finalImageList = gson.toJson(imageList, listType);
            UpdateItemOutcome outcome = table.updateItem("userId", input.getUserId(), new AttributeUpdate("imageUrls").put(finalImageList));
            Item finalResult = table.getItem("userId", input.getUserId());
            return new InsertImageResponse(gson.fromJson(finalResult.getString("imageUrls"),listType),true,"status 200 ok");
        }
        catch (Exception e) {
            e.printStackTrace();
            return new InsertImageResponse(null,false,e.getLocalizedMessage());
        }
    }
}
