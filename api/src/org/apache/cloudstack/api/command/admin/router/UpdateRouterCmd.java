// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.api.command.admin.router;

import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.user.Account;
import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiCommandJobType;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseResponse;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.ClusterResponse;
import org.apache.cloudstack.api.response.DomainResponse;
import org.apache.cloudstack.api.response.DomainRouterResponse;
import org.apache.cloudstack.api.response.ListResponse;
import org.apache.cloudstack.api.response.PodResponse;
import org.apache.cloudstack.api.response.TemplateResponse;
import org.apache.cloudstack.api.response.UpdateRouterResponse;
import org.apache.cloudstack.api.response.ZoneResponse;
import org.apache.cloudstack.context.CallContext;

import java.util.List;
import java.util.logging.Logger;

@APICommand(name = "updateRouter", description = "Updates Running router scripts and packages", responseObject = BaseResponse.class,
        requestHasSensitiveInfo = false, responseHasSensitiveInfo = false)
public class UpdateRouterCmd extends org.apache.cloudstack.api.BaseCmd {
    public static final Logger s_logger = Logger.getLogger(UpdateRouterCmd.class.getName());
    private static final String s_name = "updaterouterresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name = ApiConstants.ID, type = CommandType.UUID, entityType = DomainRouterResponse.class, description = "Updates router with the specified Id")
    private Long id;

    @Parameter(name = ApiConstants.CLUSTER_ID,
               type = CommandType.UUID,
               entityType = ClusterResponse.class,
               description = "Updates all routers within the specified cluster")
    private Long clusterId;

    @Parameter(name = ApiConstants.POD_ID, type = CommandType.UUID, entityType = PodResponse.class, description = "Updates all routers within the specified pod")
    private Long podId;

    @Parameter(name = ApiConstants.ZONE_ID, type = CommandType.UUID, entityType = ZoneResponse.class, description = "Updates all routers within the specified zone")
    private Long zoneId;

    @Parameter(name=ApiConstants.ACCOUNT, type=CommandType.STRING,
            description="Updates all routers owned by the specified account")
    private String account;

    @Parameter(name = ApiConstants.DOMAIN_ID,
               type = CommandType.UUID,
               entityType = DomainResponse.class,
               description = "Updates all routers owned by the specified domain")
    private Long domainId;

    @Parameter(name = ApiConstants.TEMPLATE_ID,
            required = true,
            type = CommandType.UUID,
            entityType = TemplateResponse.class,
            description = "Contents of the package with specified templateId will be updated on VR")
    private Long templateId;
    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public Long getPodId() {
        return podId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public String getAccount() {
        return account;
    }

    public Long getDomainId() {
        return domainId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM; // no account info given, parent this command to SYSTEM so ERROR events are tracked
    }

    public ApiCommandJobType getInstanceType() {
        return ApiCommandJobType.DomainRouter;
    }

    public Long getInstanceId() {
        return getId();
    }

    @Override
    public void execute() throws ConcurrentOperationException, ResourceUnavailableException, InsufficientCapacityException {
        CallContext.current().setEventDetails("Updating router");
        List<Long> result = _routerService.updateRouter(this);
        if (result != null) {
            ListResponse<UpdateRouterResponse> response = _responseGenerator.createUpdateRouterResponse(result);
            response.setResponseName(getCommandName());
            setResponseObject(response);
        } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to upgrade router template");
        }
    }
}
