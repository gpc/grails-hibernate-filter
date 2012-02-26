package test

class Foo {

	String name
	Boolean enabled

	static hasMany = [bars: Bar]

	static hibernateFilters = {
		fooEnabledFilter(condition: 'enabled=1', default: true, aliasDomain: 'EnabledFoo')
		barEnabledFilter(collection: 'bars', condition: 'enabled=1', default: true)
		fooNameFilter(condition: ':name = name', types: 'string')
	}

	String toString() {
		"Foo($id):$name:$enabled"
	}
}
