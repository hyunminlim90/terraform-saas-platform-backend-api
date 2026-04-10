package click.opentofu.sprout.constants;

import java.util.List;
import java.util.Map;

public class AppConstants {
    public static final Integer TIMEOUT_SECONDS_MULTI = 60;
    public static final Integer TIMEOUT_SECONDS_SINGLE = 30;
    public static final String ROOT_PATH = "/home/ec2-user/opentofu/workspace";
    public static final String DOWNLOAD_ROOT_PATH = "/home/ec2-user/opentofu/download";

    // public static final String ROOT_PATH = "C:/Users/HP/Desktop/Git/terraform-project/terraform/workspace";

    // public static final String ROOT_PATH = "/Users/hyunminlim/Desktop/opentofu/workspace";
    // public static final String DOWNLOAD_ROOT_PATH = "/Users/hyunminlim/Desktop/opentofu/download";

    public static final Map<String, List<String>> TEXTAREA_PARAMS_BY_MODULE = Map.of(

        "aws_vpc", List.of(),
        "aws_vpc_ipam_pool", List.of(),
        "aws_instance", List.of("user_data"),
        "aws_subnet", List.of(),
        "aws_security_group", List.of(),
        "aws_internet_gateway", List.of(),
        "aws_route_table", List.of()
        
    );
}
