package RenderReferencedFeatureChain {
    view chained {
        render renderings.asTree;
    }
    view chainedWithBody {
        render renderings.asTree {
            redefines depth = settings.tree.depth;
        }
    }
    view chainedAndTyped {
        render renderings.asTree : TreeRendering [1];
    }
    view anonymousDeclaration {
        render rendering : TreeRendering;
    }
}
