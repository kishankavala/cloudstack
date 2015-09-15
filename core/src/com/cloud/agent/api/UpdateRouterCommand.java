//
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
//

package com.cloud.agent.api;

import com.cloud.agent.api.routing.NetworkElementCommand;

public class UpdateRouterCommand extends NetworkElementCommand {

    private String localFile;
    String poolUuid;
    private String remoteTargetDirectory;

    @Override
    public boolean executeInSequence() {
        return false;
    }

    public UpdateRouterCommand(String poolUuid, String localFile, String remoteTargetDirectory) {
        super();
        this.poolUuid = poolUuid;
        this.localFile = localFile;
        this.remoteTargetDirectory = remoteTargetDirectory;
    }

    public String getPoolUuid() {
        return poolUuid;
    }

    public String getLocalFile() {
        return localFile;
    }

    public String getRemoteTargetDirectory() {
        return remoteTargetDirectory;
    }
}
