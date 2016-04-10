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

package org.drausin.bitflow.streams.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.bitcoinj.core.Sha256Hash;
import org.drausin.bitflow.blockchain.api.objects.BlockHeader;

public interface KafkaConsumerProducerFactory {

    KafkaConsumer<Sha256Hash, BlockHeader> createBlockHeaderConsumer();

    KafkaProducer<Sha256Hash, BlockHeader> createBlockHeaderProducer();
}
