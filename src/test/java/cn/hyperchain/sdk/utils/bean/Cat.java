package cn.hyperchain.sdk.utils.bean;

/**
 * @author Lam
 * @ClassName Cat
 * @date 2020/1/2
 */
public class Cat<T> implements Animal {
    @Override
    public String name() {
        return "cat1";
    }
}
