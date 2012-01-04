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

import org.apache.log4j.Logger;

import voldemort.cluster.Node;
import voldemort.server.ServiceType;
import voldemort.server.VoldemortConfig;
import voldemort.server.niosocket.NioSocketService;
import voldemort.server.protocol.RequestHandlerFactory;

/**
 * Factory for quickly constructing socket services from configuration
 * parameters, providing an abstraction layer on top of the socket services
 * constructed.
 * 
 * @author Robert Butler
 * 
 */
public final class SocketServiceFactory {

    private static final Logger logger = Logger.getLogger(SocketServiceFactory.class);

    private SocketServiceFactory() {}

    /**
     * Constructs a socket configuration given a service type, node and global
     * configuration object.
     * 
     * @param serviceType The type of service to construct. Must be ADMIN or
     *        SOCKET.
     * @param identityNode The node configuration information for the node this
     *        socket is running on.
     * @param config The global Voldemort configuration object.
     * @return A socket configuration for the given service type.
     */
    public static SocketServiceConfig socketServiceConfig(ServiceType serviceType,
                                                          Node identityNode,
                                                          VoldemortConfig config) {
        return new SocketServiceConfig(serviceType, config, identityNode);
    }

    /**
     * Constructs a socket service using the most general parameters. A socket
     * configuration is constructed from the given service type, node and
     * Voldemort configuration. This method is equivalent to calling
     * <tt>newSocketService(SocketServiceConfig, RequestHandlerFactory)</tt>
     * with the resulting socket configuration and request handler factory.
     * 
     * @param serviceType The type of service to construct. Must be ADMIN or
     *        SOCKET.
     * @param identityNode The node configuration information for the node this
     *        socket is running on.
     * @param config The global Voldemort configuration object.
     * @param reqHandlerFactory The factory to use to construct request handlers
     *        in the new socket service.
     * @return A newly constructed socket service.
     */
    public static SocketService newSocketService(ServiceType serviceType,
                                                 Node identityNode,
                                                 VoldemortConfig config,
                                                 RequestHandlerFactory reqHandlerFactory) {
        SocketServiceConfig ssConfig = socketServiceConfig(serviceType, identityNode, config);
        return newSocketService(ssConfig, reqHandlerFactory);
    }

    /**
     * Constructs a new (unstarted) socket service from the given configuration
     * and request handler factory.
     * 
     * @param config The socket service configuration specifying all necessary
     *        parameters for the socket service type being constructed.
     * @param reqHandlerFactory The factory to construct request handlers for
     *        incoming socket requests.
     * @return A newly constructed socket service.
     */
    public static SocketService newSocketService(SocketServiceConfig config,
                                                 RequestHandlerFactory reqHandlerFactory) {
        ServiceType serviceType = config.getServiceType();
        SocketService service = null;
        if(config.getUseNioConnector()) {
            logger.info("Creating '" + serviceType + "' service using NIO server");
            service = new NioSocketService();
        } else {
            logger.info("Creating '" + serviceType + "' service using BIO server");
            service = new BioSocketService();
        }
        service.configure(config, reqHandlerFactory);
        return service;
    }

    /**
     * Constructs a new admin service from the given global configuration, node
     * configuration and request handler factory.
     * 
     * @param identityNode The node configuration for the node this service is
     *        running on.
     * @param config The global Voldemort configuration.
     * @param reqHandlerFactory The factory to construct request handlers for
     *        incoming socket requests.
     * @return A newly constructed admin socket service.
     */
    public static SocketService newAdminSocketService(Node identityNode,
                                                      VoldemortConfig config,
                                                      RequestHandlerFactory reqHandlerFactory) {
        return newSocketService(ServiceType.ADMIN, identityNode, config, reqHandlerFactory);
    }

    /**
     * Constructs a new socket service from the given global configuration, node
     * configuration and request handler factory.
     * 
     * @param identityNode The node configuration for the node this service is
     *        running on.
     * @param config The global Voldemort configuration.
     * @param reqHandlerFactory The factory to construct request handlers for
     *        incoming socket requests.
     * @return A newly constructed socket service.
     */
    public static SocketService newCoreSocketService(Node identityNode,
                                                     VoldemortConfig config,
                                                     RequestHandlerFactory reqHandlerFactory) {
        return newSocketService(ServiceType.SOCKET, identityNode, config, reqHandlerFactory);
    }

}
