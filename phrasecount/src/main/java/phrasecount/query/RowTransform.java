/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package phrasecount.query;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import phrasecount.pojos.PhraseAndCounts;

public class RowTransform implements Function<Iterator<Entry<Key, Value>>, PhraseAndCounts> {
  @Override
  public PhraseAndCounts apply(Iterator<Entry<Key, Value>> input) {
    String phrase = null;

    int totalPhraseCount = 0;
    int docPhraseCount = 0;

    while (input.hasNext()) {
      Entry<Key, Value> colEntry = input.next();
      String cq = colEntry.getKey().getColumnQualifierData().toString();

      if (cq.equals(PhraseCountTable.TOTAL_PC_CQ)) {
        totalPhraseCount = Integer.parseInt(colEntry.getValue().toString());
      } else {
        docPhraseCount = Integer.parseInt(colEntry.getValue().toString());
      }

      if (phrase == null) {
        phrase = colEntry.getKey().getRowData().toString();
      }
    }

    return new PhraseAndCounts(phrase, docPhraseCount, totalPhraseCount);
  }
}
