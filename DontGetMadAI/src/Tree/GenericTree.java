package Tree;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GenericTree<E>
{
    private GenericTreeNode<E> root;
    private ArrayList<GenericTree<E>> children;
    private GenericTree<E> parent;

    public GenericTree(E value)
    {
        root = new GenericTreeNode<>();
        children = new ArrayList<>();
        parent = null;
        setValue(value);
    }

    public E getValue()
    {
        return root.value;
    }

    public void setValue(E value)
    {
        root.value = value;
    }

    public E[] getChildrenVals(Class<E> c)
    {
        E[] children = (E[]) Array.newInstance(c, this.children.size());
        for (int i = 0; i < this.children.size(); i++)
        {
            children[i] = this.children.get(i).getValue();
        }
        return children;
    }

    public GenericTree<E>[] getChildren()
    {
        return children.toArray(new GenericTree[]{});
    }

    public void setChildren(ArrayList<GenericTree<E>> children)
    {
        for (GenericTree<E> child : children)
        {
            this.addChild(child.getValue());
        }
    }

    public void setChildren(GenericTree<E>[] children)
    {
        this.children.clear();
        for (GenericTree<E> child : children)
        {
            this.addChild(child.getValue());
        }
    }

    public void addChild(E child)
    {
        children.add(new GenericTree<>(child));
        children.get(children.size() - 1).setParent(this);
    }

    public void addChildren(E[] children)
    {
        for (E child : children)
        {
            addChild(child);
        }
    }

    public GenericTree<E> getParent()
    {
        return parent;
    }

    public void setParent(GenericTree<E> parent)
    {
        this.parent = parent;
    }
}
