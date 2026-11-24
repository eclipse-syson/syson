package ViewRenderingComments {
    package Components {
        part engine;
    }

    concern def Reliability {
        subject;
    }
    concern reliability : Reliability;
    viewpoint def Overview {
        frame reliability;
    }
    rendering def Diagram;

    view def OverviewView {
        viewpoint overviewPoint : Overview;
        render rendering diagram : Diagram;
    }

    view overview : OverviewView {
        expose Components::*;
        render diagram; // Keep the rendering explanation.
        // Keep the final view-body comment.
    }
}
