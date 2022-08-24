package identity.entity;

import identityMaster.entity.RootIdentity;

import java.io.File;
import java.util.Stack;

public class HashedTextToAction implements TextToAction {

    private final RootIdentity rootIdentity;


    public HashedTextToAction(File rootMap) {
        this.rootIdentity = null; //TODO, parse from file YAML?
    }


    @Override
    public Identity identify(StringBuilder sb) {
        return rootIdentity.identify(sb);
    }

}
