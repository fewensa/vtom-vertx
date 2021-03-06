package io.vtom.vertx.pipeline.lifecycle.skip;

import io.vtom.vertx.pipeline.step.VtmStepContext;

import java.util.HashSet;
import java.util.Set;

public class VtmSkipContext {

  private Set<String> skipIdList;
  private Set<Integer> skipOrds;
  private boolean all;

  VtmSkipContext() {
  }


  public static VtmSkipContext context(Skip skip) {
    return skip.context;
  }

  Skip skip() {
    return new Skip(this);
  }


  VtmSkipContext id(String id) {
    if (this.skipIdList == null)
      this.skipIdList = new HashSet<>();
    this.skipIdList.add(id);
    return this;
  }

  VtmSkipContext id(Set<String> skipIdList) {
    this.skipIdList = skipIdList;
    return this;
  }

  VtmSkipContext ord(Integer ord) {
    if (this.skipOrds == null)
      this.skipOrds = new HashSet<>();
    this.skipOrds.add(ord);
    return this;
  }

  VtmSkipContext ord(Set<Integer> skipOrds) {
    this.skipOrds = skipOrds;
    return this;
  }

  VtmSkipContext all() {
    return this.all(true);
  }

  VtmSkipContext all(boolean all) {
    this.all = all;
    return this;
  }

  public boolean skip(VtmStepContext stepcontext) {
    if (this.all)
      return true;

    if (this.skipIdList != null) {
      if (this.skipIdList.contains(stepcontext.id()))
        return true;
    }

    if (this.skipOrds != null) {
      if (this.skipOrds.contains(stepcontext.ord()))
        return true;
    }
    return false;
  }


}
