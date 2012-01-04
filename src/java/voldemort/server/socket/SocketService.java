/*
 * Copyright 2008-2009 LinkedIn, Inc
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
import voldemort.server.VoldemortService;
import voldemort.server.protocol.RequestHandlerFactory;

/**
 * The VoldemortService that loads up the socket server
 * 
 */

public interface SocketService extends VoldemortService {

    /**
     * Configures the socket service with the given configuration information
     * and request handler factory. The specifics of the configuration are
     * implementation specific, but in general socket services can't be used
     * until configure is successfully called once. Also, most services may be
     * configured at most one time.
     * 
     * @param config The socket service configuration object.
     * @param reqHandlerFactory The request handler factory to create objects
     *        for handling incoming requests.
     * 
     * @author Robert Butler
     */
    void configure(SocketServiceConfig config, RequestHandlerFactory reqHandlerFactory);

    /**
     * Returns a StatusManager instance for use with status reporting tools.
     * 
     * @return StatusManager
     */

    StatusManager getStatusManager();
}
