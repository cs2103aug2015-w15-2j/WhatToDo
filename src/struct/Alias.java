package struct;

public class Alias implements Comparable<Alias> {

	private String alias;
	private String original;

	public Alias(String alias, String original) {
		this.alias = alias;
		this.original = original;
	}

	public String getAlias() {
		return alias;
	}

	public String getOriginal() {
		return original;
	}

	public boolean isUserDefined() {
		return !alias.equals(original);
	}

	@Override
	public String toString() {
		// Display both the alias and original if it is user defined
		if (isUserDefined()) {
			return alias + " : " + original;
		} else {
			return alias;
		}
	}

	@Override
	public int compareTo(Alias b) {
		if (this.alias.equals(b.alias)) {
			return this.original.compareTo(b.original);
		} else {
			return this.alias.compareTo(b.alias);
		}
	}
}
