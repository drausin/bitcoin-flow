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

package org.drausin.bitflow.bitcoin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.bitcoin.api.BitcoinNodeService;
import org.drausin.bitflow.bitcoin.api.objects.BitcoindRpcRequest;
import org.drausin.bitflow.bitcoin.api.objects.BitcoindRpcResponse;
import org.drausin.bitflow.bitcoin.api.providers.BlockHeaderResponseMapperProvider;
import org.drausin.bitflow.bitcoin.api.providers.BlockchainInfoResponseMapperProvider;
import org.drausin.bitflow.bitcoin.config.ServerConfig;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;
import org.drausin.bitflow.blockchain.api.objects.BlockchainInfo;
import org.drausin.bitflow.service.utils.BitflowResource;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

public final class BitcoinNodeResource extends BitflowResource implements BitcoinNodeService {

    public static final String BLOCKCHAIN_INFO_RPC_METHOD = "getinfo";
    public static final String BLOCK_HEADER_RPC_METHOD = "getblock";

    private final ServerConfig config;
    private final HttpAuthenticationFeature rpcAuth;

    private final WebTarget blockchainInfoTarget;
    private final WebTarget blockHeaderTarget;

    public BitcoinNodeResource(@JsonProperty ServerConfig config) {
        this.config = config;
        this.rpcAuth = HttpAuthenticationFeature.basic(config.getBitcoinNode().getRpcUser(),
                config.getBitcoinNode().getRpcPassword());
        this.blockchainInfoTarget = getBlockchainInfoClient().target(config.getRpcUri());
        this.blockHeaderTarget = getBlockHeaderClient().target(config.getRpcUri());
    }

    public ServerConfig getConfig() {
        return config;
    }

    public HttpAuthenticationFeature getRpcAuth() {
        return rpcAuth;
    }

    public WebTarget getBlockchainInfoTarget() {
        return blockchainInfoTarget;
    }

    public WebTarget getBlockHeaderTarget() {
        return blockHeaderTarget;
    }

    public Client getBlockchainInfoClient() {
        return getBlockchainInfoClient(ClientBuilder.newClient());
    }

    public Client getBlockchainInfoClient(Client current) {
        return getCommonClient(current).register(BlockchainInfoResponseMapperProvider.class);
    }

    public Client getBlockHeaderClient() {
        return getBlockHeaderClient(ClientBuilder.newClient());
    }

    public Client getBlockHeaderClient(Client current) {
        return getCommonClient(current).register(BlockHeaderResponseMapperProvider.class);
    }

    private Client getCommonClient(Client current) {
        return current
                .register(getRpcAuth())
                .register(JacksonFeature.class);
    }

    @Override
    public BitcoindRpcResponse getBlockchainInfo() throws IllegalStateException {
        BitcoindRpcRequest request = BitcoindRpcRequest.of(BLOCKCHAIN_INFO_RPC_METHOD, ImmutableList.of());
        BitcoindRpcResponse response = readResponse(getBlockchainInfoTarget(), request);
        response.validateResult(BlockchainInfo.class);
        return response;
    }

    @Override
    public BitcoindRpcResponse getBlockHeader(Sha256Hash headerHash) throws IllegalStateException {
        BitcoindRpcRequest request = BitcoindRpcRequest.of(BLOCK_HEADER_RPC_METHOD, ImmutableList.of(headerHash));
        BitcoindRpcResponse response = readResponse(getBlockHeaderTarget(), request);
        response.validateResult(BlockHeader.class);
        return response;
    }

    private static BitcoindRpcResponse readResponse(WebTarget target, BitcoindRpcRequest request) {
        return target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), BitcoindRpcResponse.class);
    }
}