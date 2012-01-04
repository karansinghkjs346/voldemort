/*
 * Copyright 2012 Pancake Technology, LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package voldemort.server.socket;

import voldemort.cluster.Node;
import voldemort.server.ServiceType;
import voldemort.server.VoldemortConfig;

public class SocketServiceConfig {

    private VoldemortConfig config;
    private Node identityNode;
    private ServiceType serviceType;

    public SocketServiceConfig(ServiceType serviceType, VoldemortConfig config, Node identityNode) {
        if(serviceType != ServiceType.ADMIN && serviceType != ServiceType.HTTP
           && serviceType != ServiceType.SOCKET)
            throw new IllegalArgumentException("Unexpected service type '"
                                               + serviceType
                                               + "' for socket service (expected one of ADMIN, HTTP or SOCKET).");
        this.serviceType = serviceType;
        this.config = config;
        this.identityNode = identityNode;
    }

    /**
     * Default constructor that can be used for custom configuration
     * implementations.
     */
    protected SocketServiceConfig() {}

    public ServiceType getServiceType() {
        return serviceType;
    }

    public int getPort() {
        switch(serviceType) {
            case ADMIN:
                return identityNode.getAdminPort();
            case HTTP:
                return identityNode.getHttpPort();
            case SOCKET:
                return identityNode.getSocketPort();
            default:
                throw new IllegalStateException("No port for service type '" + serviceType + "'");
        }
    }

    public String getServiceName() {
        switch(serviceType) {
            case ADMIN:
                return "admin-server";
            case HTTP:
                return "VoldemortHttp";
            case SOCKET:
                /*
                 * if (config.isSslEnabled()) return "ssl-socket-server"; else
                 * if (config.isUseNettyServer()) return "netty-socket-server";
                 * else
                 */
                if(config.getUseNioConnector())
                    return "nio-socket-server";
                else
                    return "socket-server";
            default:
                throw new IllegalStateException("No service name for service type '" + serviceType
                                                + "'");
        }
    }

    public boolean isJmxEnabled() {
        return config.isJmxEnabled();
    }

    public int getSocketBufferSize() {
        return config.getSocketBufferSize();
    }

    public int getSelectors() {
        return config.getNioConnectorSelectors();
    }

    public int getCoreThreads() {
        return config.getCoreThreads();
    }

    public int getMaxThreads() {
        return config.getMaxThreads();
    }

    public boolean getUseNioConnector() {
        return config.getUseNioConnector();
    }

}
