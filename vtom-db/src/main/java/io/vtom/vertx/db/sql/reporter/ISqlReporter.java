/*
 * Copyright (c) 2018, enoa (fewensa@enoa.io)
 *
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
package io.vtom.vertx.db.sql.reporter;

import io.enoa.toolkit.random.RandomKit;
import io.vertx.core.json.JsonArray;

/**
 * sql reporter interface
 */
public interface ISqlReporter {

  static ISqlReporter def() {
    return _VtomSqlReporter.instance();
  }

  default String mark() {
    return String.valueOf(RandomKit.nextInt(100, 999));
  }

  void report(String mark, String sql, JsonArray paras);

}
