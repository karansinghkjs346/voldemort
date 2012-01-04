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

package voldemort;

import voldemort.server.ServiceType;
import voldemort.server.socket.SocketServiceConfig;

public class TestSocketServiceConfig extends SocketServiceConfig {

    protected boolean useNio;
    protected int port;
    protected int coreConnections;
    protected int maxConnections;
    protected int selectors;
    protected int bufferSize;
    protected String serviceName;
    protected boolean jmxEnabled;
    protected ServiceType serviceType;

    public TestSocketServiceConfig(boolean useNio,
                                   int port,
                                   int coreConnections,
                                   int maxConnections,
                                   int selectors,
                                   int bufferSize,
                                   String serviceName,
                                   boolean jmxEnabled,
                                   ServiceType serviceType) {
        super();
        this.useNio = useNio;
        this.port = port;
        this.coreConnections = coreConnections;
        this.maxConnections = maxConnections;
        this.bufferSize = bufferSize;
        this.serviceName = serviceName;
        this.jmxEnabled = jmxEnabled;
        this.serviceType = serviceType;
        this.selectors = selectors;
    }

    public TestSocketServiceConfig(boolean useNio,
                                   int port,
                                   int coreConnections,
                                   int maxConnections,
                                   int selectors,
                                   int bufferSize,
                                   String serviceName,
                                   boolean jmxEnabled) {
        this(useNio,
             port,
             coreConnections,
             maxConnections,
             selectors,
             bufferSize,
             serviceName,
             jmxEnabled,
             ServiceType.SOCKET);
    }

    @Override
    public int getSelectors() {
        return selectors;
    }

    @Override
    public ServiceType getServiceType() {
        return serviceType;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int getSocketBufferSize() {
        return bufferSize;
    }

    @Override
    public int getCoreThreads() {
        return coreConnections;
    }

    @Override
    public int getMaxThreads() {
        return maxConnections;
    }

    @Override
    public boolean getUseNioConnector() {
        return useNio;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

}
