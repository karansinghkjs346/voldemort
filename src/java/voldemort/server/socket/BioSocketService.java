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

import voldemort.server.StatusManager;
import voldemort.server.protocol.RequestHandlerFactory;

public class BioSocketService extends AbstractSocketService {

    private SocketServer server;

    public StatusManager getStatusManager() {
        if(server == null)
            throw new IllegalStateException("BIO socket service not configured.");
        return this.server.getStatusManager();
    }

    @Override
    public void configure(SocketServiceConfig config, RequestHandlerFactory reqHandlerFactory) {
        super.configure(config, reqHandlerFactory);

        this.server = new SocketServer(config.getPort(),
                                       config.getCoreThreads(),
                                       config.getMaxThreads(),
                                       config.getSocketBufferSize(),
                                       reqHandlerFactory,
                                       config.getServiceName());
    }

    @Override
    protected void startInner() {
        this.server.start();
        this.server.awaitStartupCompletion();
        enableJmx(server);
    }

    @Override
    protected void stopInner() {
        this.server.shutdown();
    }

}
