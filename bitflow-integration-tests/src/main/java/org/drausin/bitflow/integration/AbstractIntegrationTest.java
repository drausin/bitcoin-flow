/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drausin.bitflow.integration;

import com.google.common.base.Optional;
import com.palantir.remoting.http.FeignClientFactory;
import com.palantir.remoting.http.FeignClients;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.providers.BitcoinNodeMapperProvider;

/**
 * Abstract class handling setup and configuration for all integration tests.
 *
 * This integration test framework assumes that docker-compose environment is up and running via something like
 *
 * <pre>
 * docker-compose -f docker-compose.yml --x-networking -p bitflow up -d
 * </pre>
 *
 * @author dwulsin
 */
public abstract class AbstractIntegrationTest {

    // can get IP via `docker-machine ip boot2docker-vm`
    private static final String BITCOIN_NODE_URI = "http://192.168.99.100:8332";

    private static final FeignClientFactory CLIENT_FACTORY = FeignClients.withMapper(
            BitcoinNodeMapperProvider.getMapper());

    private final BitcoinNodeService bitcoinNode;

    public AbstractIntegrationTest() {
        this(CLIENT_FACTORY.createProxy(Optional.absent(), BITCOIN_NODE_URI, BitcoinNodeService.class));
    }

    public AbstractIntegrationTest(BitcoinNodeService bitcoinNode) {
        this.bitcoinNode = bitcoinNode;
    }

    public BitcoinNodeService getBitcoinNode() {
        return bitcoinNode;
    }
}
