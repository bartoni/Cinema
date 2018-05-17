public interface HierarchicalController<P extends HierarchicalController<?>> {

    P getParentController();
    void setParentController(P parent);

}