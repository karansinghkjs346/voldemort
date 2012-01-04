/*
 * Copyright 2009 Mustard Grain, Inc.
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

import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.MBeanOperationInfo;

import org.apache.log4j.Logger;

import voldemort.annotations.jmx.JmxGetter;
import voldemort.annotations.jmx.JmxManaged;
import voldemort.annotations.jmx.JmxOperation;
import voldemort.server.ServiceType;
import voldemort.server.VoldemortService;
import voldemort.server.protocol.RequestHandlerFactory;
import voldemort.utils.JmxUtils;
import voldemort.utils.Utils;

/**
 * AbstractSocketService abstracts the different implementations so that we can
 * use this common super class by various callers.
 * 
 */

@JmxManaged(description = "A server that handles remote operations on stores via TCP/IP.")
public abstract class AbstractSocketService implements VoldemortService, SocketService {

    private static final Logger logger = Logger.getLogger(AbstractSocketService.class);

    private final AtomicBoolean isStarted;
    private ServiceType type;
    private boolean configured;

    private int port;
    private String serviceName;
    private boolean enableJmx;

    public AbstractSocketService() {
        this.isStarted = new AtomicBoolean(false);
        this.configured = false;
    }

    /**
     * Configures the socket service with the given configuration information
     * and request handler factory. The specifics of the configuration are
     * implementation specific. This socket services that extend from this class
     * can't be used until configure is successfully called exactly once. Any
     * subclasses that override this method MUST call <tt>super.configure()</tt>
     * or the service will not be properly configured.
     */
    public void configure(SocketServiceConfig config, RequestHandlerFactory reqHandlerFactory) {
        // Simple failure test; proper synchronization is probably unnecessary
        // since configuration typically happens on a single thread anyway
        if(this.configured)
            throw new IllegalStateException("Socket service already configured.");
        this.type = Utils.notNull(config.getServiceType());
        this.serviceName = config.getServiceName();
        this.enableJmx = config.isJmxEnabled();
        this.port = config.getPort();
        this.configured = true;
    }

    public ServiceType getType() {
        return type;
    }

    @JmxGetter(name = "started", description = "Determine if the service has been started.")
    public boolean isStarted() {
        return isStarted.get();
    }

    @JmxOperation(description = "Start the service.", impact = MBeanOperationInfo.ACTION)
    public void start() {
        if(!configured)
            throw new IllegalStateException("Service is not configured, socket services must be configured before start() is called.");

        boolean isntStarted = isStarted.compareAndSet(false, true);
        if(!isntStarted)
            throw new IllegalStateException("Server is already started!");

        logger.info("Starting " + getType().getDisplayName());
        startInner();
    }

    @JmxOperation(description = "Stop the service.", impact = MBeanOperationInfo.ACTION)
    public void stop() {
        logger.info("Stopping " + getType().getDisplayName());
        synchronized(this) {
            if(!isStarted()) {
                logger.info("The service is already stopped, ignoring duplicate attempt.");
            }

            stopInner();
            isStarted.set(false);
        }
    }

    /**
     * Simply retrieves the port on which this service is listening for incoming
     * requests.
     * 
     * @return Port number
     */

    @JmxGetter(name = "port", description = "The port on which the server is accepting connections.")
    public final int getPort() {
        return port;
    }

    protected String getServiceName() {
        return serviceName;
    }

    /**
     * Getter for determining if JMX is enabled for this socket service.
     * 
     * @return True if JMX is enabled, false otherwise.
     */
    protected boolean isJmxEnabled() {
        return enableJmx;
    }

    /**
     * If JMX is enabled, will register the given object under the service name
     * with which this class was created.
     * 
     * @param obj Object to register as an MBean
     */

    protected void enableJmx(Object obj) {
        if(enableJmx)
            JmxUtils.registerMbean(serviceName, obj);
    }

    protected abstract void startInner();

    protected abstract void stopInner();

}
