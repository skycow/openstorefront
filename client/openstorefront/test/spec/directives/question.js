'use strict';

describe('Directive: question', function () {

  // load the directive's module
  beforeEach(module('openstorefrontApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<question></question>');
    element = $compile(element)(scope);
    expect(element.html()).toBe('');
  }));
});
