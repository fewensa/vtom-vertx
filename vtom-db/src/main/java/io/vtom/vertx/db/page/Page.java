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
package io.vtom.vertx.db.page;

import io.enoa.toolkit.convert.IConverter;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable, IConverter<Page<JsonObject>, Page<ResultSet>> {

  private int pn;
  private int ps;
  // total page
  private int tpg;
  private long offset;
  // total rows
  private long trw;
  private List<T> rows;

  public static <J> Page<J> converter() {
    return new Page<>();
  }

  public Page() {
  }

  public Page(int pageNumber, int pageSize, int totalPage, long offset, long totalRow, List<T> rows) {
    this.pn = pageNumber;
    this.ps = pageSize;
    this.tpg = totalPage;
    this.trw = totalRow;
    this.rows = rows;
    this.offset = offset;
  }

  public int getPn() {
    return pn;
  }

  public Page<T> setPn(int pn) {
    this.pn = pn;
    return this;
  }

  public int getPs() {
    return ps;
  }

  public Page<T> setPs(int ps) {
    this.ps = ps;
    return this;
  }

  public int getTpg() {
    return tpg;
  }

  public Page<T> setTpg(int tpg) {
    this.tpg = tpg;
    return this;
  }

  public long getOffset() {
    return offset;
  }

  public Page<T> setOffset(long offset) {
    this.offset = offset;
    return this;
  }

  public long getTrw() {
    return trw;
  }

  public Page<T> setTrw(long trw) {
    this.trw = trw;
    return this;
  }

  public List<T> getRows() {
    return rows;
  }

  public Page<T> setRows(List<T> rows) {
    this.rows = rows;
    return this;
  }

  @Override
  public String toString() {
    return "Page{" +
      "pn=" + pn +
      ", ps=" + ps +
      ", tpg=" + tpg +
      ", offset=" + offset +
      ", trw=" + trw +
      ", rows=" + rows +
      '}';
  }

  @Override
  public Page<JsonObject> convert(Page<ResultSet> page) {
    this.pn = page.pn;
    this.ps = page.ps;
    this.tpg = page.tpg;
    this.offset = page.offset;
    this.trw = page.trw;
    List<ResultSet> rows = page.rows;
    ResultSet rs = rows.get(0);
    this.rows = (List<T>) rs.getRows();
    return (Page<JsonObject>) this;
  }

}
